package com.drunkmod.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.TradeOffers;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(TradeOffers.class)
public class VillagerTradesMixin {
    // In modern Minecraft (1.21+), the keys in the trade map are RegistryKeys, not the objects themselves.
    @Shadow @Final public static Map<RegistryKey<VillagerProfession>, Int2ObjectMap<TradeOffers.Factory[]>> PROFESSION_TO_LEVELED_TRADE;
    @Shadow @Final public static Map<RegistryKey<VillagerProfession>, Int2ObjectMap<TradeOffers.Factory[]>> REBALANCED_PROFESSION_TO_LEVELED_TRADE;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void drunkmod$clearClericTrades(CallbackInfo ci) {
        // We find the Cleric profession in the standard and rebalanced maps and clear its trade list.
        // This effectively removes all default Minecraft trades for Clerics.
        clearCleric(PROFESSION_TO_LEVELED_TRADE);
        clearCleric(REBALANCED_PROFESSION_TO_LEVELED_TRADE);
    }

    private static void clearCleric(Map<RegistryKey<VillagerProfession>, Int2ObjectMap<TradeOffers.Factory[]>> map) {
        if (map == null) return;
        Int2ObjectMap<TradeOffers.Factory[]> clericTrades = map.get(VillagerProfession.CLERIC);
        if (clericTrades != null) {
            clericTrades.clear();
        }
    }
}
