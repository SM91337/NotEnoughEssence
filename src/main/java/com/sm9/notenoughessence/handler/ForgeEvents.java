package com.sm9.notenoughessence.handler;

import com.sm9.notenoughessence.command.Reload;
import com.sm9.notenoughessence.util.General;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.sm9.notenoughessence.NotEnoughEssence.maCrafting;
import static com.sm9.notenoughessence.NotEnoughEssence.neeLogger;
import static com.sm9.notenoughessence.common.Config.MainConfig.*;

public class ForgeEvents {
    public static ArrayList<String> blacklistedMobs;

    public static void preInit(FMLPreInitializationEvent evEvent) {
        MinecraftForge.EVENT_BUS.register(new ForgeEvents());

        File configDirectory = new File(evEvent.getModConfigurationDirectory(), "sm9/NotEnoughEssence");
        File configFile = new File(configDirectory, "main.cfg");

        neeLogger = LogManager.getLogger("NotEnoughEssence");
        mainConfig = new Configuration(configFile);
        blacklistedMobs = new ArrayList<>();
    }

    public static void postInit(FMLPostInitializationEvent evEvent) {
        loadConfig();
    }

    public static void onWorldLoad(FMLServerStartingEvent evEvent) {
        evEvent.registerServerCommand(new Reload());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDrops(LivingDropsEvent evEvent) {
        List<EntityItem> entityDrops = evEvent.getDrops();

        if (entityDrops == null) {
            return;
        }

        ItemStack itemStack;
        ArrayList<EntityItem> dropsToRemove = new ArrayList<>();

        for (EntityItem item : entityDrops) {
            itemStack = item.getItem();

            if (!Objects.requireNonNull(itemStack.getItem().getRegistryName()).toString().equals("mysticalagriculture:crafting") || itemStack.getMetadata() != 0) {
                continue;
            }

            dropsToRemove.add(item);
        }

        entityDrops.removeAll(dropsToRemove);

        DamageSource damageSource = evEvent.getSource();
        Entity damageEntity = damageSource.getTrueSource();

        if (!(damageEntity instanceof EntityPlayer)) {
            return;
        }

        EntityLivingBase damageVictim = evEvent.getEntityLiving();

        if (damageVictim == null) {
            return;
        }

        World localWorld = damageVictim.world;
        EntityPlayer localPlayer = (EntityPlayer) damageEntity;

        if (localWorld == null) {
            return;
        }

        if (!(damageVictim instanceof IMob) && !damageVictim.isCreatureType(EnumCreatureType.MONSTER, false)) {
            return;
        }

        int iLocalDimension = localPlayer.dimension;

        if (meleeOnly && damageSource.isProjectile()) {
            if (debugMode) {
                General.printToPlayer(localPlayer, "Ignoring none melee kill.");
            }
            return;
        }

        if (noZombies && damageVictim instanceof EntityZombie) {
            if (debugMode) {
                General.printToPlayer(localPlayer, "Ignoring zombie.");
            }
            return;
        }

        if (General.mobBlacklisted(damageVictim)) {
            if (debugMode) {
                General.printToPlayer(localPlayer, "Ignoring blacklisted mob: %s", General.findEntityIdByClass(damageVictim.getClass()));
            }
            return;
        }

        if (nightOnly && localWorld.isDaytime() && iLocalDimension != -1) {
            if (debugMode) {
                General.printToPlayer(localPlayer, "Ignoring kill, must be night time.");
            }
            return;
        }

        Random rRandom = worldRandom ? localWorld.rand : new Random();
        int iRandom = Math.round(rRandom.nextFloat() * 100.0f);

        if (debugMode) {
            General.printToPlayer(localPlayer, "Drop Chance: %d, RNG: %d, Should Drop: %s", dropChance, iRandom, iRandom <= dropChance ? "true" : "false");
        }

        if (iRandom <= dropChance) {
            evEvent.getDrops().add(new EntityItem(localWorld, damageVictim.posX, damageVictim.posY, damageVictim.posZ, new ItemStack(maCrafting, 1)));
        }
    }
}