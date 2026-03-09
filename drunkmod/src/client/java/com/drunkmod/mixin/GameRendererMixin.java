package com.drunkmod.mixin;

import com.drunkmod.effect.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks into the game render tick to apply nausea-like distortion for drunk players.
 *
 * The vanilla "nausea" effect sets client.player.nauseaIntensity which drives
 * the waving/distortion shader in GameRenderer.
 * We inject into GameRenderer.tick() and artificially drive nauseaIntensity based
 * on how drunk the player is, WITHOUT any poison overlay.
 *
 * amplifier 0 → subtle wobble (0.15)  — Tipsy
 * amplifier 1 → medium wobble (0.32)  — Drunk
 * amplifier 2 → heavy wobble (0.55)   — Very Drunk
 * amplifier 3 → wasted wobble (0.75)  — Absolutely Wasted
 */
@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private MinecraftClient client;

//    @Inject(method = "tick", at = @At("HEAD"))
//    private void drunkmod$onRenderTick(CallbackInfo ci) {
//        if (client.player == null) return;
//
//        StatusEffectInstance drunkEffect = client.player.getStatusEffect(ModEffects.DRUNK);
//        if (drunkEffect != null) {
//            float[] intensityByLevel = { 0.25f, 0.45f, 0.65f, 0.85f };
//            int amp = Math.max(0, Math.min(drunkEffect.getAmplifier(), intensityByLevel.length - 1));
//            float targetIntensity = intensityByLevel[amp];
//
//            float current = client.player.nauseaIntensity;
//            if (current < targetIntensity) {
//                client.player.nauseaIntensity = Math.min(current + 0.01f, targetIntensity);
//            } else if (current > targetIntensity) {
//                client.player.nauseaIntensity = Math.max(current - 0.005f, targetIntensity);
//            }
//        }
//    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void drunkmod$onRenderTick(CallbackInfo ci) {
        if (client.player == null) return;

        StatusEffectInstance drunkEffect = client.player.getStatusEffect(ModEffects.DRUNK);
        if (drunkEffect != null) {
            // Stronger wobble values — vanilla nausea tops out around 1.0
            float[] intensityByLevel = { 0.30f, 0.55f, 0.75f, 1.0f };
            int amp = Math.max(0, Math.min(drunkEffect.getAmplifier(), intensityByLevel.length - 1));
            float targetIntensity = intensityByLevel[amp];

            float current = client.player.nauseaIntensity;
            float speed = amp >= 2 ? 0.02f : 0.01f; // ramps up faster at high levels
            if (current < targetIntensity) {
                client.player.nauseaIntensity = Math.min(current + speed, targetIntensity);
            } else if (current > targetIntensity) {
                client.player.nauseaIntensity = Math.max(current - 0.005f, targetIntensity);
            }
        } else {
            // Fade out when effect ends
            if (client.player.nauseaIntensity > 0) {
                client.player.nauseaIntensity = Math.max(0, client.player.nauseaIntensity - 0.01f);
            }
        }
    }
}
