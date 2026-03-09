package com.drunkmod.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.server.world.ServerWorld;

public class DrunkEffect extends StatusEffect {

    public DrunkEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFFD700); // Golden color
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        // The visual effects (nausea/wobble) are handled client-side via the mixin.
        // Here we can add gameplay effects based on amplifier (drunk level).
        if (entity instanceof PlayerEntity player) {
            // At higher drunk levels, occasionally stumble (random velocity nudge)
            if (amplifier >= 2 && world.random.nextInt(100) == 0) {
                double angle = world.random.nextDouble() * Math.PI * 2;
                entity.addVelocity(Math.cos(angle) * 0.15, 0, Math.sin(angle) * 0.15);
            }
        }
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // Apply update every second
        return duration % 20 == 0;
    }
}
