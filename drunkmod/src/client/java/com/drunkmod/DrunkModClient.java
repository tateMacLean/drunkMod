package com.drunkmod;

import com.drunkmod.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;

public class DrunkModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModParticles.registerParticleFactories();
        DrunkMod.LOGGER.info("DrunkMod client initialized!");
    }
}
