package com.drunkmod.trade;

import com.drunkmod.DrunkMod;
import com.drunkmod.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;

import java.util.Optional;

/**
 * Registers alcoholic drink trades with villagers.
 */
public class DrunkModTrades {

    public static void registerTrades() {

        // ── FARMER sells Beer (Novice, tier 1) ─────────────────────────────
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 1, factories -> {
            factories.add((entity, random, world) -> new TradeOffer(
                new TradedItem(Items.EMERALD, 2),
                Optional.empty(),
                new ItemStack(ModItems.BEER, 3),
                12, 5, 0.05f
            ));
        });

        // ── FARMER sells Mead (Apprentice, tier 2) ─────────────────────────
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2, factories -> {
            factories.add((entity, random, world) -> new TradeOffer(
                new TradedItem(Items.EMERALD, 3),
                Optional.empty(),
                new ItemStack(ModItems.MEAD, 2),
                10, 10, 0.05f
            ));
        });

        // ── BUTCHER sells Wine (Journeyman, tier 3) ────────────────────────
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.BUTCHER, 3, factories -> {
            factories.add((entity, random, world) -> new TradeOffer(
                new TradedItem(Items.EMERALD, 4),
                Optional.empty(),
                new ItemStack(ModItems.WINE, 2),
                8, 15, 0.05f
            ));
        });

        // ── CLERIC sells Rum (Expert, tier 4) ──────────────────────────────
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 4, factories -> {
            factories.add((entity, random, world) -> new TradeOffer(
                new TradedItem(Items.EMERALD, 6),
                Optional.empty(),
                new ItemStack(ModItems.RUM, 1),
                6, 20, 0.05f
            ));
        });

        // ── CLERIC sells Whiskey (Master, tier 5) ──────────────────────────
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 5, factories -> {
            factories.add((entity, random, world) -> new TradeOffer(
                new TradedItem(Items.EMERALD, 10),
                Optional.empty(),
                new ItemStack(ModItems.WHISKEY, 1),
                4, 30, 0.05f
            ));
        });

        DrunkMod.LOGGER.info("Registered DrunkMod villager trades.");
    }
}
