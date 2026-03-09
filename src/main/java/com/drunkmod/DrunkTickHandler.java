package com.drunkmod;

import com.drunkmod.effect.ModEffects;
import com.drunkmod.particle.ModParticles;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Runs every server tick to spawn floating bubble particles around drunk players.
 * The frequency and count of bubbles scales with the amplifier (drunk level).
 */
public class DrunkTickHandler {

    private static int tickCounter = 0;

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

                // Spawn bubbles much less frequently
                int spawnInterval = switch (amp) {
                    case 0 -> 120; // Every 6 seconds
                    case 1 -> 80;  // Every 4 seconds
                    case 2 -> 60;  // Every 3 seconds
                    default -> 40; // Every 2 seconds
                };

                if (tickCounter % spawnInterval != 0) continue;

                int count = 1; // Only 1 bubble per spawn event

                for (int i = 0; i < count; i++) {
                    double ox = (world.random.nextDouble() - 0.5) * 0.6;
                    double oy = world.random.nextDouble() * 0.3;
                    double oz = (world.random.nextDouble() - 0.5) * 0.6;

                    world.spawnParticles(
                        ModParticles.DRUNK_BUBBLE,
                        player.getX() + ox,
                        player.getY() + 0.5 + oy,
                        player.getZ() + oz,
                        1,     // count (we're already looping)
                        0,     // spread x
                        0.05,  // spread y (slight upward spread)
                        0,     // spread z
                        0.03   // speed
                    );
                }
            }
        }
    }
}
