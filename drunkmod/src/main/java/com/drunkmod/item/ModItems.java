package com.drunkmod.item;

import com.drunkmod.DrunkMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    // ─────────────────────────────────────────────
    // DRINKS
    // drunkDuration in ticks (20 = 1s), amplifier 0-3
    // ─────────────────────────────────────────────

    /** Light beer — tipsy, short */
    public static final AlcoholicDrinkItem BEER = new AlcoholicDrinkItem(
        new Item.Settings(), 1200, 0   // 60s, level 1
    );

    /** Mead — medium strength */
    public static final AlcoholicDrinkItem MEAD = new AlcoholicDrinkItem(
        new Item.Settings(), 1600, 1   // 80s, level 2
    );

    /** Wine — medium-long */
    public static final AlcoholicDrinkItem WINE = new AlcoholicDrinkItem(
        new Item.Settings(), 2000, 1   // 100s, level 2
    );

    /** Rum — strong */
    public static final AlcoholicDrinkItem RUM = new AlcoholicDrinkItem(
        new Item.Settings(), 2400, 2   // 120s, level 3
    );

    /** Whiskey — very strong */
    public static final AlcoholicDrinkItem WHISKEY = new AlcoholicDrinkItem(
        new Item.Settings(), 3000, 3   // 150s, level 4 (maximum chaos)
    );

    public static void registerItems() {
        register("beer",    BEER);
        register("mead",    MEAD);
        register("wine",    WINE);
        register("rum",     RUM);
        register("whiskey", WHISKEY);

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

    private static void register(String name, Item item) {
        Registry.register(Registries.ITEM, Identifier.of(DrunkMod.MOD_ID, name), item);
    }
}
