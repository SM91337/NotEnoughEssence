package com.sm9.notenoughessence;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Random;

import static com.sm9.notenoughessence.NotEnoughEssence.*;

public class ForgeEventHandlers
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDrops(LivingDropsEvent evEvent)
    {
        ArrayList<EntityItem> entityDrops = (ArrayList) evEvent.getDrops();

        if(entityDrops == null) {
            return;
        }

        DamageSource damageSource = evEvent.getSource();
        Entity damageEntity = damageSource.getTrueSource();

        if(damageEntity == null || !(damageEntity instanceof EntityPlayer)) {
            return;
        }

        Entity damageVictim = evEvent.getEntity();

        if(damageVictim == null || !damageVictim.isCreatureType(EnumCreatureType.MONSTER, false)) {
            return;
        }

        World localWorld = damageVictim.world;
        EntityPlayer localPlayer = (EntityPlayer) damageEntity;

        if(localPlayer == null || localWorld == null) {
            return;
        }

        int iLocalDimension = localPlayer.dimension;

        if(g_bMeleeOnly && damageSource.isProjectile()) {
            if(g_bDebugMode) {
                String sMessage = String.format("[NEE] Ignoring none melee kill.");
                localPlayer.sendMessage(new TextComponentString(sMessage));
            }
            return;
        }

        if(g_bNoZombies && damageVictim instanceof EntityZombie && !(damageVictim instanceof EntityPigZombie)) {
            if(g_bDebugMode) {
                String sMessage = String.format("[NEE] Ignoring zombie.");
                localPlayer.sendMessage(new TextComponentString(sMessage));
            }
            return;
        }

        if(g_bNightOnly && localWorld.isDaytime() && iLocalDimension != -1) {
            if(g_bDebugMode) {
                String sMessage = String.format("[NEE] Ignoring kill, must be night time.");
                localPlayer.sendMessage(new TextComponentString(sMessage));
            }
            return;
        }

        Random rRandom = g_bWorldRandom ? localWorld.rand : new Random();
        int iRandom = Math.round(rRandom.nextFloat() * 100.0f);

        if(iRandom <= g_iChance) {
            evEvent.getDrops().add(new EntityItem(localWorld, damageVictim.posX, damageVictim.posY, damageVictim.posZ, new ItemStack(maCrafting, 1)));
        }

        if(g_bDebugMode) {
            String sMessage = String.format("[NEE] Drop Chance: %d, RNG: %d, Should Drop: %s", g_iChance, iRandom, iRandom < g_iChance ? "true" : "false");
            localPlayer.sendMessage(new TextComponentString(sMessage));
        }
    }
}