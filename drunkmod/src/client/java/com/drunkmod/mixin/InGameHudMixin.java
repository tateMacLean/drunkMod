package com.drunkmod.mixin;

import com.drunkmod.effect.ModEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void drunkmod$cancelPortalOverlay(DrawContext context, float alpha, CallbackInfo ci) {
        // If the player only has our DRUNK effect and NOT the vanilla nausea, cancel the purple portal overlay
        if (client.player != null && client.player.hasStatusEffect(ModEffects.DRUNK) && !client.player.hasStatusEffect(net.minecraft.entity.effect.StatusEffects.NAUSEA)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderNauseaOverlay", at = @At("HEAD"), cancellable = true)
    private void drunkmod$cancelNauseaOverlay(DrawContext context, float alpha, CallbackInfo ci) {
        // If the player only has our DRUNK effect and NOT the vanilla nausea, cancel the purple nausea overlay
        if (client.player != null && client.player.hasStatusEffect(ModEffects.DRUNK) && !client.player.hasStatusEffect(net.minecraft.entity.effect.StatusEffects.NAUSEA)) {
            ci.cancel();
        }
    }
}
