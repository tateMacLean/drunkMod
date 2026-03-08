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
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
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
        super(settings.maxCount(16));
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
    public SoundEvent getDrinkSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient()) {
            // Apply or stack the drunk effect
            StatusEffectInstance existing = user.getStatusEffect(ModEffects.DRUNK);
            int newDuration = drunkDuration;
            int newAmplifier = drunkAmplifier;

            if (existing != null) {
                // Stack duration, and increase amplifier if drinking more
                newDuration = Math.min(existing.getDuration() + drunkDuration, drunkDuration * 4);
                newAmplifier = Math.min(existing.getAmplifier() + 1, 3); // Max level 4 (amplifier 3)
            }

            user.addStatusEffect(new StatusEffectInstance(
                ModEffects.DRUNK,
                newDuration,
                newAmplifier,
                false,
                true,
                true
            ));

            // Spawn bubble particles on the server side
            if (world instanceof ServerWorld serverWorld) {
                for (int i = 0; i < 10; i++) {
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
