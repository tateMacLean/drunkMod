package com.drunkmod.trade;

import com.drunkmod.DrunkMod;
import com.drunkmod.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

/**
 * Registers alcoholic drink trades with villagers.
 *
 * Brewer (replaced by Butcher/Farmer since there's no brewer in vanilla):
 *   - Farmer villagers sell Beer and Mead (early tiers)
 *   - Butcher/Cleric villagers sell Wine, Rum, Whiskey (higher tiers)
 *
 * Trade format: TradeOffer(buy1, buy2, sell, uses, xp, multiplier)
 */
public class DrunkModTrades {

    public static void registerTrades() {

        // ── FARMER sells Beer (Novice, tier 1) ─────────────────────────────
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 1, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                new ItemStack(Items.EMERALD, 2),     // cost: 2 emeralds
                new ItemStack(ModItems.BEER, 3),      // reward: 3 beers
                12, 5, 0.05f
            ));
        });

        // ── FARMER sells Mead (Apprentice, tier 2) ─────────────────────────
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                new ItemStack(Items.EMERALD, 3),
                new ItemStack(ModItems.MEAD, 2),
                10, 10, 0.05f
            ));
        });

        // ── BUTCHER sells Wine (Journeyman, tier 3) ────────────────────────
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.BUTCHER, 3, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                new ItemStack(Items.EMERALD, 4),
                new ItemStack(ModItems.WINE, 2),
                8, 15, 0.05f
            ));
        });

        // ── CLERIC sells Rum (Expert, tier 4) ──────────────────────────────
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 4, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                new ItemStack(Items.EMERALD, 6),
                new ItemStack(ModItems.RUM, 1),
                6, 20, 0.05f
            ));
        });

        // ── CLERIC sells Whiskey (Master, tier 5) ──────────────────────────
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 5, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                new ItemStack(Items.EMERALD, 10),
                new ItemStack(ModItems.WHISKEY, 1),
                4, 30, 0.05f
            ));
        });

        DrunkMod.LOGGER.info("Registered DrunkMod villager trades.");
    }
}
