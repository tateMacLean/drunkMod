package com.drunkmod.item;

import com.drunkmod.effect.ModEffects;
import com.drunkmod.particle.ModParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Base class for all alcoholic beverages.
 * 
 * drunkDuration  - how long the drunk effect lasts (in ticks, 20 ticks = 1 second)
 * drunkAmplifier - strength of the drunk effect (0 = tipsy, 1 = drunk, 2 = wasted)
 */
public class AlcoholicDrinkItem extends Item {

    private final int drunkDuration;
    private final int drunkAmplifier;

    public AlcoholicDrinkItem(Settings settings, int drunkDuration, int drunkAmplifier) {
        super(settings);
        this.drunkDuration = drunkDuration;
        this.drunkAmplifier = drunkAmplifier;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32; // Same as drinking a potion
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        return net.minecraft.item.ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient()) {
            // Apply or stack the drunk effect
            StatusEffectInstance existing = user.getStatusEffect(ModEffects.DRUNK);
            int newDuration = drunkDuration;
            int newAmplifier = drunkAmplifier;

            if (existing != null) {
                // Stack duration: existing + new, up to 10 minutes (12000 ticks)
                newDuration = Math.min(existing.getDuration() + drunkDuration, 12000);
                
                // Smart Stacking: 
                // 1. If the new drink is stronger than your current state, jump to that strength + 1.
                // 2. If the new drink is weaker, just increment the current strength by 1.
                newAmplifier = Math.max(existing.getAmplifier() + 1, drunkAmplifier);
                
                // Cap at 10
                newAmplifier = Math.min(newAmplifier, 10);
            }

            user.addStatusEffect(new StatusEffectInstance(
                ModEffects.DRUNK,
                newDuration,
                newAmplifier,
                false,
                true,
                true
            ));

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_GENERIC_DRINK, user.getSoundCategory(), 1.0f, 1.0f);

            // Spawn fewer bubble particles
            if (world instanceof ServerWorld serverWorld) {
                for (int i = 0; i < 3; i++) {
                    double ox = (world.random.nextDouble() - 0.5) * 0.8;
                    double oy = world.random.nextDouble() * 0.5;
                    double oz = (world.random.nextDouble() - 0.5) * 0.8;
                    serverWorld.spawnParticles(
                        ModParticles.DRUNK_BUBBLE,
                        user.getX(), user.getY() + 1.5, user.getZ(),
                        1, ox, oy, oz, 0.05
                    );
                }
            }

            // Return an empty glass bottle
            if (user instanceof PlayerEntity player && !player.isCreative()) {
                stack.decrement(1);
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (stack.isEmpty()) {
                    return bottle;
                }
                player.getInventory().insertStack(bottle);
            }
        }

        return stack.isEmpty() ? new ItemStack(Items.GLASS_BOTTLE) : stack;
    }
}
