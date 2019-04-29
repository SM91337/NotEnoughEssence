package com.sm9.notenoughessence.command;

import com.sm9.notenoughessence.common.Config.MainConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

public class Reload extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "neereload";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender commandSender) {
        return "neereload";
    }

    @Override
    public void execute(@Nonnull MinecraftServer localServer, ICommandSender commandSender, @Nonnull String[] sArgs) {
        MainConfig.loadConfig();
        commandSender.sendMessage(new TextComponentString("Not Enough Essence config reloaded successfully!"));
    }
}