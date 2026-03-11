package com.drunkmod;

import com.drunkmod.effect.ModEffects;
import com.drunkmod.particle.ModParticles;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.ChunkStatus;

/**
 * Runs every server tick to handle drunk player effects, including particle spawning
 * and the "blackout" teleportation feature.
 */
public class DrunkTickHandler {

    private static int tickCounter = 0;
    
    // Blackout thresholds
    // Level 5 (amplifier 4) is when you START having a random chance.
    private static final int BLACKOUT_MIN_AMPLIFIER = 6;
    // Level 10 (amplifier 9) is when a blackout is GUARANTEED (100% chance).
    private static final int BLACKOUT_GUARANTEED_AMPLIFIER = 9;
    
    // Base chance for blackout per tick (roughly once every 4 minutes if sustained)
    private static final double BLACKOUT_CHANCE = 0.0002;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(DrunkTickHandler::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        tickCounter++;

        for (ServerWorld world : server.getWorlds()) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                StatusEffectInstance drunk = player.getStatusEffect(ModEffects.DRUNK);
                if (drunk == null) continue;

                int amp = drunk.getAmplifier();

                // ── BUBBLE PARTICLES ──────────────────────────────────────────
                handleParticles(world, player, amp);

                // ── BLACKOUT FEATURE ──────────────────────────────────────────
                if (amp >= BLACKOUT_GUARANTEED_AMPLIFIER) {
                    triggerBlackout(world, player);
                } 
                else if (amp >= BLACKOUT_MIN_AMPLIFIER) {
                    double currentChance = BLACKOUT_CHANCE * (amp - BLACKOUT_MIN_AMPLIFIER + 1);
                    if (world.random.nextDouble() < currentChance) {
                        triggerBlackout(world, player);
                    }
                }
            }
        }
    }

    private static void handleParticles(ServerWorld world, ServerPlayerEntity player, int amp) {
        int spawnInterval = switch (amp) {
            case 0 -> 120;
            case 1 -> 80;
            case 2 -> 60;
            default -> 40;
        };

        if (tickCounter % spawnInterval == 0) {
            double ox = (world.random.nextDouble() - 0.5) * 0.6;
            double oy = world.random.nextDouble() * 0.3;
            double oz = (world.random.nextDouble() - 0.5) * 0.6;

            world.spawnParticles(
                ModParticles.DRUNK_BUBBLE,
                player.getX() + ox,
                player.getY() + 0.5 + oy,
                player.getZ() + oz,
                1, 0, 0.05, 0, 0.03
            );
        }
    }

    private static void triggerBlackout(ServerWorld world, ServerPlayerEntity player) {
        DrunkMod.LOGGER.info("Player {} is blacking out!", player.getName().getString());

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 200, 0, false, false));
        player.removeStatusEffect(ModEffects.DRUNK);

        // Random radius up to 5,000 blocks
        int radius = 5000;
        int destX = (int) player.getX() + (world.random.nextInt(radius * 2) - radius);
        int destZ = (int) player.getZ() + (world.random.nextInt(radius * 2) - radius);
        
        // ── ROBUST SURFACE FINDING ──────────────────────────────────────────
        
        // 1. Force the chunk to be FULLY loaded/generated. 
        // ChunkStatus.FULL ensures the terrain and features are all there.
        world.getChunkManager().getChunk(destX >> 4, destZ >> 4, ChunkStatus.FULL, true);
        
        // 2. Use the most reliable heightmap
        int destY = world.getTopY(Heightmap.Type.MOTION_BLOCKING, destX, destZ);
        
        // 3. Manual Scan Fallback: If Y seems too low (like in a cave or at world bottom),
        // or if it's just plain air, scan down from the sky.
        BlockPos.Mutable checkPos = new BlockPos.Mutable(destX, destY, destZ);
        
        // If we are in air, scan down until we hit something solid.
        // If we hit nothing but air down to sea level, just use sea level.
        while (world.getBlockState(checkPos).isAir() && checkPos.getY() > world.getBottomY()) {
            checkPos.move(0, -1, 0);
        }
        
        // If we are INSIDE a block (suffocating), move up until we find air.
        while (!world.getBlockState(checkPos).isAir() && checkPos.getY() < world.getTopY(Heightmap.Type.MOTION_BLOCKING, destX, destZ)) {
            checkPos.move(0, 1, 0);
        }
        
        destY = checkPos.getY();

        // 4. Final safety sanity check
        if (destY <= world.getBottomY() + 10) {
            destY = world.getSeaLevel() + 10;
        }

        player.requestTeleport(destX + 0.5, destY + 0.1, destZ + 0.5);
        player.sendMessage(Text.literal("You blacked out and woke up somewhere else..."), true);
    }
}
