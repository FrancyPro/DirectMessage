package net.daechler.directmessage.bungee;

import lombok.Getter;
import net.daechler.directmessage.bungee.Commands.DisableMsgCommand;
import net.daechler.directmessage.bungee.Commands.MessageCommand;
import net.daechler.directmessage.bungee.Commands.ReplyCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public class DirectMessage extends Plugin {
    @Getter
    private static DirectMessage instance;

    @Override
    public void onEnable() {
        instance = this;

        getProxy().getPluginManager().registerCommand(this, new MessageCommand());
        getProxy().getPluginManager().registerCommand(this, new ReplyCommand());
        getProxy().getPluginManager().registerCommand(this, new DisableMsgCommand());
        getLogger().info(ChatColor.GREEN + "DirectMessage has been enabled!");
    }
}
