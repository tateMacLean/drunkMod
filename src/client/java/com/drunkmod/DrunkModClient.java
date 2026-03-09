package com.drunkmod;

import com.drunkmod.particle.DrunkBubbleParticle;
import com.drunkmod.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class DrunkModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance()
            .register(ModParticles.DRUNK_BUBBLE, DrunkBubbleParticle.Factory::new);
        DrunkMod.LOGGER.info("DrunkMod client initialized!");
    }
}
