package com.sm9.notenoughessence;

import com.sm9.notenoughessence.handler.ForgeEvents;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod
        (
                modid = Constants.MOD_ID,
                name = Constants.MOD_NAME,
                version = Constants.MOD_VERSION,
                dependencies = Constants.DEPENDENCIES,
                acceptedMinecraftVersions = Constants.MINECRAFT_VERSION,
                acceptableRemoteVersions = "*"
        )
public
class NotEnoughEssence {
    public static Logger neeLogger;
    public static Item maCrafting;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evEvent) {
        ForgeEvents.preInit(evEvent);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evEvent) {
        ForgeEvents.postInit(evEvent);
    }

    @Mod.EventHandler
    public void onWorldLoad(FMLServerStartingEvent evEvent) {
        ForgeEvents.onWorldLoad(evEvent);
    }
}