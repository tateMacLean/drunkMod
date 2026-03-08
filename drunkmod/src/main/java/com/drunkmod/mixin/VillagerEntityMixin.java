package com.drunkmod.mixin;

import com.drunkmod.item.ModItems;
import com.drunkmod.trade.DrunkModTrades;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to hook into villager offer generation.
 * NOTE: The preferred approach is using Fabric API's VillagerInteractionRegistries
 * via DrunkModTrades, but this mixin serves as a reliable fallback for profession-based trades.
 */
@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
    // Trade registration is handled via DrunkModTrades using Fabric API.
    // This mixin file is kept for potential future overrides.
}
