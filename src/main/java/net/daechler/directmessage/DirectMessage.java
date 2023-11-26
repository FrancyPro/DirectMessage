package net.daechler.directmessage;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.*;

public class DirectMessage extends Plugin implements Listener {

    // Map for storing last sender for each player
    public Map<UUID, UUID> lastSenderMap = new HashMap<>();
    // Set for storing players who have disabled receiving messages
    public Set<UUID> msgDisabledSet = new HashSet<>();

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new MessageCommand(this));
        getProxy().getPluginManager().registerCommand(this, new ReplyCommand(this));
        getProxy().getPluginManager().registerCommand(this, new DisableMessageCommand(this));
        getLogger().info(ChatColor.GREEN + "DirectMessage has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "DirectMessage has been disabled!");
    }

    public void sendMessage(ProxiedPlayer sender, ProxiedPlayer receiver, String message) {
        if (msgDisabledSet.contains(receiver.getUniqueId())) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "This player has disabled messages."));
            return;
        }

        String formattedMessage = ChatColor.translateAlternateColorCodes('&',
                "&e&lYou &r&7» &e&l" + receiver.getName() + "&7:&r " + message);
        String formattedMessageForReceiver = ChatColor.translateAlternateColorCodes('&',
                "&e&l" + sender.getName() + " &r&7» &e&lYou&7:&r " + message);

        // Added console logging
        getLogger().info(ChatColor.stripColor("Private Message: " + sender.getName() + " » " + receiver.getName() + ": " + message));

        sender.sendMessage(new TextComponent(formattedMessage));
        receiver.sendMessage(new TextComponent(formattedMessageForReceiver));

        for (ProxiedPlayer player : getProxy().getPlayers()) {
            if (player.hasPermission("directmessage.spy") && !player.equals(sender) && !player.equals(receiver)) {
                player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                        "&4&l[SPY]&r &e&l" + sender.getName() + " &r&7» &e&l" + receiver.getName() + "&7:&r " + message)));
            }
        }

        lastSenderMap.put(receiver.getUniqueId(), sender.getUniqueId());
    }

    public class MessageCommand extends Command {
        private DirectMessage plugin;

        public MessageCommand(DirectMessage plugin) {
            super("msg");
            this.plugin = plugin;
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
            plugin.sendMessage((ProxiedPlayer) sender, receiver, message);
        }
    }

    public class ReplyCommand extends Command {
        private DirectMessage plugin;

        public ReplyCommand(DirectMessage plugin) {
            super("r");
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (!(sender instanceof ProxiedPlayer) || args.length < 1) {
                sender.sendMessage(new TextComponent("Usage: /r <Message>"));
                return;
            }

            UUID lastSenderId = plugin.lastSenderMap.get(((ProxiedPlayer) sender).getUniqueId());
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
            plugin.sendMessage((ProxiedPlayer) sender, lastSender, message);
        }
    }

    public class DisableMessageCommand extends Command {
        private DirectMessage plugin;

        public DisableMessageCommand(DirectMessage plugin) {
            super("disablemsg");
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(new TextComponent("Only players can disable messages!"));
                return;
            }

            UUID playerId = ((ProxiedPlayer) sender).getUniqueId();
            if (plugin.msgDisabledSet.contains(playerId)) {
                plugin.msgDisabledSet.remove(playerId);
                sender.sendMessage(new TextComponent(ChatColor.GREEN + "Messages enabled!"));
            } else {
                plugin.msgDisabledSet.add(playerId);
                sender.sendMessage(new TextComponent(ChatColor.RED + "Messages disabled!"));
            }
        }
    }
}
