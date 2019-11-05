package com.extracraftx.minecraft.programmerartinjector;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;

public class ProgrammerArtInjector implements ClientModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "programmer_art_injector";
    public static final String MOD_NAME = "Programmer Art Injector";

    @Override
    public void onInitializeClient() {
        log(Level.INFO, "Initialized.");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}
