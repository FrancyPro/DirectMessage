package net.daechler.directmessage.bungee.Commands;

import net.daechler.directmessage.bungee.Apis.MessageApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

public class MessageCommand extends Command {
    public MessageCommand() {
        super("msg");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer) || args.length < 2) {
            sender.sendMessage(new TextComponent("Usage: /msg <Name> <Message>"));
            return;
        }

        ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(args[0]);
        if (receiver == null) {
            sender.sendMessage(new TextComponent("Player not found."));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        MessageApi.sendMessage((ProxiedPlayer) sender, receiver, message);
    }
}
