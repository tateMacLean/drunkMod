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

                // Spawn bubbles less frequently at lower amplifiers
                int spawnInterval = switch (amp) {
                    case 0 -> 15;  // Tipsy:  every 15 ticks
                    case 1 -> 10;  // Drunk:  every 10 ticks
                    case 2 -> 6;   // Very drunk: every 6 ticks
                    default -> 3;  // Wasted: every 3 ticks
                };

                if (tickCounter % spawnInterval != 0) continue;

                int count = amp + 2; // 2–5 bubbles per spawn event

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
