package net.daechler.directmessage.bungee.Commands;

import net.daechler.directmessage.bungee.Apis.MessageApi;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class DisableMsgCommand extends Command {
    public DisableMsgCommand() {
        super("disablemsg");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("Only players can disable messages!"));
            return;
        }

        UUID playerId = ((ProxiedPlayer) sender).getUniqueId();
        if (MessageApi.getMsgDisabledSet().contains(playerId)) {
            MessageApi.getMsgDisabledSet().remove(playerId);
            sender.sendMessage(new TextComponent(ChatColor.GREEN + "Messages enabled!"));
        } else {
            MessageApi.getMsgDisabledSet().add(playerId);
            sender.sendMessage(new TextComponent(ChatColor.RED + "Messages disabled!"));
        }
    }
}
