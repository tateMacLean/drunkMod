package com.drunkmod.effect;

import com.drunkmod.DrunkMod;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static RegistryEntry<StatusEffect> DRUNK;

    public static void registerEffects() {
        DRUNK = Registry.registerReference(
            Registries.STATUS_EFFECT,
            Identifier.of(DrunkMod.MOD_ID, "drunk"),
            new DrunkEffect()
        );
        DrunkMod.LOGGER.info("Registered drunk effect.");
    }
}
