package com.drunkmod;

import com.drunkmod.effect.ModEffects;
import com.drunkmod.item.ModItems;
import com.drunkmod.trade.DrunkModTrades;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrunkMod implements ModInitializer {

    public static final String MOD_ID = "drunkmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("DrunkMod initializing...");
        ModEffects.registerEffects();
        com.drunkmod.particle.ModParticles.registerParticles();
        ModItems.registerItems();
        DrunkModTrades.registerTrades();
        DrunkTickHandler.register();
        LOGGER.info("DrunkMod initialized! Cheers!");
    }
}
