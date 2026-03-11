package com.drunkmod.item;

import com.drunkmod.DrunkMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {

    // ─────────────────────────────────────────────
    // DRINKS
    // drunkDuration in ticks (20 = 1s), amplifier 0-3
    // ─────────────────────────────────────────────

    /** Light beer — tipsy, short */
    public static final AlcoholicDrinkItem BEER = register("beer", 1200, 0);

    /** Mead — medium strength */
    public static final AlcoholicDrinkItem MEAD = register("mead", 1600, 2);

    /** Wine — medium-long */
    public static final AlcoholicDrinkItem WINE = register("wine", 2000, 2);

    /** Rum — strong */
    public static final AlcoholicDrinkItem RUM = register("rum", 2400, 3);

    /** Whiskey — very strong */
    public static final AlcoholicDrinkItem WHISKEY = register("whiskey", 3000, 3);

    public static void registerItems() {
        // Static fields are initialized when the class is loaded.
        // We call this from DrunkMod to ensure class loading.

        // Add to the Food & Drinks group in creative inventory
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(BEER);
            entries.add(MEAD);
            entries.add(WINE);
            entries.add(RUM);
            entries.add(WHISKEY);
        });

        DrunkMod.LOGGER.info("Registered all DrunkMod items.");
    }

    private static AlcoholicDrinkItem register(String name, int duration, int amplifier) {
        Identifier id = Identifier.of(DrunkMod.MOD_ID, name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        
        AlcoholicDrinkItem item = new AlcoholicDrinkItem(
            new Item.Settings()
                .registryKey(key)
                .maxCount(16)
                .component(DataComponentTypes.CONSUMABLE, ConsumableComponents.drink().build()),
            duration,
            amplifier
        );
        
        return Registry.register(Registries.ITEM, key, item);
    }
}
