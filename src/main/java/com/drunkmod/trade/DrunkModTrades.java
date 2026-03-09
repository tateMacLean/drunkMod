package com.drunkmod.trade;

import com.drunkmod.DrunkMod;
import com.drunkmod.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;

import java.util.Optional;

/**
 * Registers alcoholic drink trades with villagers.
 */
public class DrunkModTrades {

    public static void registerTrades() {
        // Now only Clerics trade for alcohol, starting from level 1 through level 5.
        registerAlcoholForProfession(VillagerProfession.CLERIC);

        DrunkMod.LOGGER.info("Registered mixed DrunkMod villager trades for Cleric only.");
    }

    private static void registerAlcoholForProfession(RegistryKey<VillagerProfession> profession) {
        for (int level = 1; level <= 5; level++) {
            TradeOfferHelper.registerVillagerOffers(profession, level, factories -> {
                // Beer
                factories.add((entity, random, world) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 2),
                    Optional.empty(),
                    new ItemStack(ModItems.BEER, 3),
                    12, 5, 0.05f
                ));
                // Mead
                factories.add((entity, random, world) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 3),
                    Optional.empty(),
                    new ItemStack(ModItems.MEAD, 2),
                    10, 10, 0.05f
                ));
                // Wine
                factories.add((entity, random, world) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 4),
                    Optional.empty(),
                    new ItemStack(ModItems.WINE, 2),
                    8, 15, 0.05f
                ));
                // Rum
                factories.add((entity, random, world) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 6),
                    Optional.empty(),
                    new ItemStack(ModItems.RUM, 1),
                    6, 20, 0.05f
                ));
                // Whiskey
                factories.add((entity, random, world) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 10),
                    Optional.empty(),
                    new ItemStack(ModItems.WHISKEY, 1),
                    4, 30, 0.05f
                ));
            });
        }
    }
}
