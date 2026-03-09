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
 * Hooks into the game render loop to apply screen distortion for drunk players.
 */
@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render", at = @At("HEAD"))
    private void drunkmod$onRender(net.minecraft.client.render.RenderTickCounter tickCounter, boolean renderWorld, CallbackInfo ci) {
        if (client.player == null) return;

        StatusEffectInstance drunkEffect = client.player.getStatusEffect(ModEffects.DRUNK);
        if (drunkEffect != null) {
            // Balanced intensity: 1.0 is vanilla pufferfish, 6.0 is heavy but playable.
            float[] intensityByLevel = { 0.1f, 0.4f, 0.8f, 1.2f, 3.0f, 6.0f };
            int amp = Math.max(0, Math.min(drunkEffect.getAmplifier(), intensityByLevel.length - 1));
            float targetIntensity = intensityByLevel[amp];

            float current = client.player.nauseaIntensity;
            // Smoother ramp up
            if (current < targetIntensity) {
                client.player.nauseaIntensity = Math.min(current + 0.05f, targetIntensity);
            } else if (current > targetIntensity) {
                client.player.nauseaIntensity = Math.max(current - 0.02f, targetIntensity);
            }
        } else {
            if (client.player.nauseaIntensity > 0) {
                client.player.nauseaIntensity = Math.max(0, client.player.nauseaIntensity - 0.02f);
            }
        }
    }
}
