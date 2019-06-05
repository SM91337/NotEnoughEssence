package com.sm9.notenoughessence.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.Level;

import java.util.Formatter;

import static com.sm9.notenoughessence.NotEnoughEssence.neeLogger;
import static com.sm9.notenoughessence.common.Config.MainConfig.blacklistConfig;
import static com.sm9.notenoughessence.handler.ForgeEvents.blacklistedMobs;

public class General {
    public static void loadBlacklistedMobs() {
        Class<? extends Entity> cLazz;

        for (String blackListedMob : blacklistConfig) {
            if (blackListedMob == null || blackListedMob.isEmpty()) {
                continue;
            }

            cLazz = EntityList.getClass(new ResourceLocation(blackListedMob));

            if (cLazz == null || !findEntityIdByClass(cLazz).equals(blackListedMob) || !EntityLiving.class.isAssignableFrom(cLazz)) {
                debugToConsole(Level.ERROR, "Invalid blacklisted mob specified: %s", blackListedMob);
                continue;
            }

            if (blacklistedMobs.contains(blackListedMob)) {
                debugToConsole(Level.WARN, "Duplicate blacklisted mob specified: %s", blackListedMob);
                continue;
            }

            debugToConsole(Level.INFO, "Successfully added blacklisted mob %s to blacklist", blackListedMob);
            blacklistedMobs.add(blackListedMob);
        }
    }

    public static boolean mobBlacklisted(EntityLivingBase damageVictim) {
        return blacklistedMobs.contains(findEntityIdByClass(damageVictim.getClass()));
    }

    public static String findEntityIdByClass(Class<? extends Entity> clazz) {
        ResourceLocation key = EntityList.getKey(clazz);
        return key == null ? null : key.toString();
    }

    public static void printToPlayer(EntityPlayer entityPlayer, String format, Object... args) {
        String message = new Formatter().format(format, args).toString();
        entityPlayer.sendMessage(new TextComponentString("[NEE] " + message));
    }

    public static void debugToConsole(Level logLevel, String format, Object... args) {
        neeLogger.log(logLevel, new Formatter().format(format, args).toString());
    }
}