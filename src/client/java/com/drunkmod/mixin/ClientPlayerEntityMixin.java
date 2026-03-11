package com.drunkmod.mixin;

import com.drunkmod.effect.ModEffects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow public float nauseaIntensity;
    @Shadow public float lastNauseaIntensity;

    /**
     * Minecraft 1.21.1 ClientPlayerEntity.tickNausea(boolean nausea)
     * This method normally decrements nauseaIntensity if the vanilla NAUSEA effect is not present.
     * We hook in AFTER to ensure our DRUNK effect can override that decrement.
     */
    @Inject(method = "tickNausea", at = @At("TAIL"))
    private void drunkmod$onTickNausea(boolean nausea, CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        StatusEffectInstance drunkEffect = player.getStatusEffect(ModEffects.DRUNK);
        if (drunkEffect != null) {
            // Intensities mapping (Level 1 to 10)
            // We'll provide more steps so Drunk I through IX feel distinct
            float[] intensityByLevel = { 
                0.05f, // Level 1 (amp 0)
                0.08f, // Level 2
                0.1f, // Level 3 (Vanilla nausea strength)
                0.5f, // Level 4
                0.8f, // Level 5
                0.85f, // Level 6
                1.0f, // Level 7
                2.0f, // Level 8
                2.5f, // Level 9
                3f  // Level 10 (Guaranteed blackout happens shortly after)
            };
            
            int amp = Math.max(0, Math.min(drunkEffect.getAmplifier(), intensityByLevel.length - 1));
            float targetIntensity = intensityByLevel[amp];

            // If we are below the target, ramp up.
            // Note: Vanilla's tickNausea just subtracted 0.05 if !nausea.
            // We need to add enough to overcome that and reach the target.
            if (this.nauseaIntensity < targetIntensity) {
                // Vanilla subtracts 0.05, so we add 0.1 to get a net +0.05
                this.nauseaIntensity = Math.min(this.nauseaIntensity + 0.1f, targetIntensity);
            } else if (this.nauseaIntensity > targetIntensity) {
                // If we are above target, we let vanilla's -0.05 work or help it if needed.
                // If we want it to decrease slower, we could add 0.02 here.
                // For now, let's just ensure it doesn't drop below target.
                this.nauseaIntensity = Math.max(this.nauseaIntensity - 0.02f, targetIntensity);
            }
        }
    }
}
