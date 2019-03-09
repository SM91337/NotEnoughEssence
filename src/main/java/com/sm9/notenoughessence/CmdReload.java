package com.sm9.notenoughessence;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import static com.sm9.notenoughessence.NotEnoughEssence.*;

public class CmdReload extends CommandBase {
    @Override
    public String getName() {
        return "neereload";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return "neereload";
    }

    @Override
    public void execute(MinecraftServer localServer, ICommandSender commandSender, String[] sArgs) throws CommandException {
        LoadConfig();
        commandSender.sendMessage(new TextComponentString("Not Enough Essence config reloaded successfully!"));
    }
}