package net.daechler.directmessage.bungee.Commands;

import net.daechler.directmessage.bungee.Apis.MessageApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class ReplyCommand extends Command {
    public ReplyCommand() {
        super("r");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer) || args.length < 1) {
            sender.sendMessage(new TextComponent("Usage: /r <Message>"));
            return;
        }

        UUID lastSenderId = MessageApi.getLastSenderMap().get(((ProxiedPlayer) sender).getUniqueId());
        if (lastSenderId == null) {
            sender.sendMessage(new TextComponent("No one to reply to."));
            return;
        }

        ProxiedPlayer lastSender = ProxyServer.getInstance().getPlayer(lastSenderId);
        if (lastSender == null) {
            sender.sendMessage(new TextComponent("Player not online."));
            return;
        }

        String message = String.join(" ", args);
        MessageApi.sendMessage((ProxiedPlayer) sender, lastSender, message);
    }
}
