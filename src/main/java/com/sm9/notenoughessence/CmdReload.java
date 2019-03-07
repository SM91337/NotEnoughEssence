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
    public String getUsage(ICommandSender sender) {
        return "neereload";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        LoadConfig();
        sender.sendMessage(new TextComponentString("Not Enough Essence config reloaded successfully!"));
    }
}