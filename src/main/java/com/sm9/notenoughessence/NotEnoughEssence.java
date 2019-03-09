package com.sm9.notenoughessence;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;

@Mod(modid = NotEnoughEssence.MODID, name = NotEnoughEssence.NAME,
        version = NotEnoughEssence.VERSION,
        dependencies = NotEnoughEssence.DEPENDENCIES,
        acceptedMinecraftVersions = "1.12.2",
        acceptableRemoteVersions = "*")

public class NotEnoughEssence
{
    public static final String MODID = "notenoughessence";
    public static final String NAME = "Not Enough Essence";
    public static final String VERSION = "${version}";
    public static final String MIN_FORGE_VER = "14.23.5.2815";
    public static final String DEPENDENCIES = "after:forge@[" + NotEnoughEssence.MIN_FORGE_VER + ",)" + ";required-after:mysticalagriculture@[1.7.0,)";

    public static Logger g_lLogger;
    public static boolean g_bDebugMode = false;
    public static boolean g_bNoZombies = true;
    public static boolean g_bNightOnly = true;
    public static boolean g_bMeleeOnly = true;
    public static boolean g_bWorldRandom = true;

    public static int g_iChance = 20;
    public static Configuration g_cConfig;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evEvent)
    {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        g_lLogger = LogManager.getLogger("NotEnoughEssence");

        File fConfigFolder = new File(evEvent.getModConfigurationDirectory(), "sm9/NotEnoughEssence");
        File fConfig = new File(fConfigFolder, "main.cfg");

        g_cConfig = new Configuration(fConfig);

        LoadConfig();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent evEvent) {
        evEvent.registerServerCommand(new CmdReload());
    }

    public static void LoadConfig() {
        g_cConfig.load();

        g_bDebugMode = g_cConfig.getBoolean("DebugMode", Configuration.CATEGORY_GENERAL, false, "Prints debug information on hostile entity death");
        g_iChance = g_cConfig.getInt("DropChance", Configuration.CATEGORY_GENERAL, 20, 0, 100, "Drop chance for hostiles");
        g_bNoZombies = g_cConfig.getBoolean("NoZombies", Configuration.CATEGORY_GENERAL, g_bNoZombies, "Exclude zombies when dropping essence");
        g_bNightOnly = g_cConfig.getBoolean("NightOnly", Configuration.CATEGORY_GENERAL, g_bNightOnly, "Only allow essence drops at night");
        g_bMeleeOnly = g_cConfig.getBoolean("MeleeOnly", Configuration.CATEGORY_GENERAL, g_bMeleeOnly, "Only allow essence drops when killed with a melee weapon");
        g_bWorldRandom = g_cConfig.getBoolean("WorldRandom", Configuration.CATEGORY_GENERAL, g_bWorldRandom, "Use world random generator instead of built in java one");

        g_cConfig.save();
    }
}