package com.sm9.notenoughessence.common.Config;

import com.sm9.notenoughessence.util.General;
import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;
import java.util.List;

import static com.sm9.notenoughessence.handler.ForgeEvents.blacklistedMobs;

public class MainConfig {
    private static final List<String> configOrder = Arrays.asList("DebugMode", "DropChance", "NoZombies", "NightOnly", "MeleeOnly", "WorldRandom", "MobBlacklist");
    public static String[] blacklistConfig;
    public static boolean debugMode = false;
    public static Configuration mainConfig;
    public static boolean noZombies = true;
    public static boolean nightOnly = true;
    public static boolean meleeOnly = true;
    public static boolean worldRandom = true;
    public static int dropChance = 20;

    public static void loadConfig() {
        blacklistedMobs.clear();
        mainConfig.load();
        mainConfig.setCategoryPropertyOrder("General", configOrder);
        debugMode = mainConfig.getBoolean("DebugMode", Configuration.CATEGORY_GENERAL, false, "Prints debug information on hostile entity death");
        dropChance = mainConfig.getInt("DropChance", Configuration.CATEGORY_GENERAL, 20, 0, 100, "Drop chance for hostiles");
        noZombies = mainConfig.getBoolean("NoZombies", Configuration.CATEGORY_GENERAL, noZombies, "Exclude zombies when dropping essence");
        nightOnly = mainConfig.getBoolean("NightOnly", Configuration.CATEGORY_GENERAL, nightOnly, "Only allow essence drops at night");
        meleeOnly = mainConfig.getBoolean("MeleeOnly", Configuration.CATEGORY_GENERAL, meleeOnly, "Only allow essence drops when killed with a melee weapon");
        worldRandom = mainConfig.getBoolean("WorldRandom", Configuration.CATEGORY_GENERAL, worldRandom, "Use world random generator instead of built in java one");
        blacklistConfig = mainConfig.getStringList("MobBlacklist", Configuration.CATEGORY_GENERAL, new String[]{"minecraft:slime"}, "Additional blacklisted mobs which should not drop essence");

        mainConfig.save();

        General.loadBlacklistedMobs();
    }
}
