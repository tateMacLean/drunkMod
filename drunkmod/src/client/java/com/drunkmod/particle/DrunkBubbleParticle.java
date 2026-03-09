package com.drunkmod.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

/**
 * A cheerful golden bubble that floats upward and wobbles slightly,
 * appearing around players who have the Drunk effect.
 */
@Environment(EnvType.CLIENT)
public class DrunkBubbleParticle extends BillboardParticle {

    private final SpriteProvider spriteProvider;

    protected DrunkBubbleParticle(ClientWorld world, double x, double y, double z,
                                  double velocityX, double velocityY, double velocityZ,
                                  SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider.getSprite(world.random));
        this.spriteProvider = spriteProvider;

        // Golden/amber bubble color
        this.red   = 1.0f;
        this.green = 0.85f;
        this.blue  = 0.2f;
        this.alpha = 0.85f;

        this.velocityX = velocityX + (random.nextDouble() - 0.5) * 0.03;
        this.velocityY = Math.abs(velocityY) + 0.04; // always float up
        this.velocityZ = velocityZ + (random.nextDouble() - 0.5) * 0.03;

        this.scale = 0.12f + random.nextFloat() * 0.1f;
        this.maxAge = 25 + random.nextInt(20); // ~1.25–2.25 seconds

        this.updateSprite(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        // Gentle sideways wobble
        this.velocityX += (random.nextDouble() - 0.5) * 0.005;
        this.velocityZ += (random.nextDouble() - 0.5) * 0.005;
        // Fade out near end of life
        if (this.age > this.maxAge - 8) {
            this.alpha = Math.max(0f, this.alpha - 0.1f);
        }
        this.updateSprite(spriteProvider);
    }

    @Override
    public ParticleTextureSheet textureSheet() {
        return ParticleTextureSheet.SINGLE_QUADS;
    }

    @Override
    protected BillboardParticle.RenderType getRenderType() {
        return BillboardParticle.RenderType.PARTICLE_ATLAS_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world,
                                       double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ,
                                       Random random) {
            return new DrunkBubbleParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);
        }
    }
}
