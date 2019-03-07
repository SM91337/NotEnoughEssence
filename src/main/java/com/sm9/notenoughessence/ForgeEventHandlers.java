package com.sm9.notenoughessence;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;

import static com.sm9.notenoughessence.NotEnoughEssence.*;

public class ForgeEventHandlers
{
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingDrops(LivingDropsEvent evEvent)
    {
        DamageSource eDamageSource = evEvent.getSource();
        Entity eDamageEntity = eDamageSource.getTrueSource();

        if(eDamageEntity == null || !(eDamageEntity instanceof EntityPlayer)) {
            return;
        }

        Entity eAttackVictim = evEvent.getEntity();

        if(eAttackVictim == null || !eAttackVictim.isCreatureType(EnumCreatureType.MONSTER, false)) {
            return;
        }

        World eWorld = eAttackVictim.world;
        EntityPlayer ePlayer = (EntityPlayer) eDamageEntity;

        if(ePlayer == null) {
            return;
        }

        if(g_bMeleeOnly && eDamageSource.isProjectile()) {
            if(g_bDebugMode) {
                String sMessage = String.format("[NEE] Ignoring none melee kill.");
                ePlayer.sendMessage(new TextComponentString(sMessage));
            }
            return;
        }

        if(g_bNoZombies && eAttackVictim instanceof EntityZombie) {
            if(g_bDebugMode) {
                String sMessage = String.format("[NEE] Ignoring zombie.");
                ePlayer.sendMessage(new TextComponentString(sMessage));
            }
            return;
        }

        if(g_bNightOnly && eWorld.isDaytime()) {
            if(g_bDebugMode) {
                String sMessage = String.format("[NEE] Ignoring kill, must be night time.");
                ePlayer.sendMessage(new TextComponentString(sMessage));
            }
            return;
        }

        Random rRandom = g_bWorldRandom ? eWorld.rand : new Random();

        int iRandom = Math.round(rRandom.nextFloat() * 100.0f);

        if(iRandom <= g_iChance) {
            evEvent.getDrops().add(new EntityItem(eWorld, eAttackVictim.posX, eAttackVictim.posY, eAttackVictim.posZ, new ItemStack(Item.getByNameOrId("mysticalagriculture:crafting"), 1)));
        }

        if(g_bDebugMode) {
            String sMessage = String.format("[NEE] Drop Chance: %d, RNG: %d, Should Drop: %s", g_iChance, iRandom, iRandom < g_iChance ? "true" : "false");
            ePlayer.sendMessage(new TextComponentString(sMessage));
        }
    }
}