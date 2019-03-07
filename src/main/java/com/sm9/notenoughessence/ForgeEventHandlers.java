package com.sm9.notenoughessence;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
        Entity eEntity = evEvent.getEntity();
        EntityPlayerSP ePlayer = Minecraft.getMinecraft().player;
        World eWorld = eEntity.world;

        if(!eEntity.isCreatureType(EnumCreatureType.MONSTER, false) || !(evEvent.getSource().getTrueSource() instanceof EntityPlayer)) {
            return;
        }

        if(g_bNoZombies && eEntity instanceof EntityZombie) {
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

        float fRandom = new Random().nextFloat();

        if(fRandom < g_fChance) {
            BlockPos Position = eEntity.getPosition();
            ItemStack ItemDrops = new ItemStack(Item.getByNameOrId("mysticalagriculture:crafting"), 1);
            evEvent.getDrops().add(new EntityItem(eEntity.getEntityWorld(), Position.getX(), Position.getY(), Position.getZ(), ItemDrops));
        }

        if(g_bDebugMode) {
            String sMessage = String.format("[NEE] Drop Chance: %.2f, RNG: %.2f, Should Drop: %s", g_fChance, fRandom, fRandom < g_fChance ? "true" : "false");
            ePlayer.sendMessage(new TextComponentString(sMessage));
        }
    }
}