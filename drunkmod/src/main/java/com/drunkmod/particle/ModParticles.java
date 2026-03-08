package com.drunkmod.particle;

import com.drunkmod.DrunkMod;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

    public static SimpleParticleType DRUNK_BUBBLE;

    /**
     * Called from main (server + client) initializer to register the particle type.
     * The factory (renderer) is registered separately in DrunkModClient.
     */
    public static void registerParticles() {
        DRUNK_BUBBLE = Registry.register(
            Registries.PARTICLE_TYPE,
            Identifier.of(DrunkMod.MOD_ID, "drunk_bubble"),
            FabricParticleTypes.simple()
        );
        DrunkMod.LOGGER.info("Registered drunk_bubble particle.");
    }

    /**
     * Called from the client initializer to hook up the visual factory.
     */
    public static void registerParticleFactories() {
        net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.getInstance()
            .register(DRUNK_BUBBLE, DrunkBubbleParticle.Factory::new);
    }
}
