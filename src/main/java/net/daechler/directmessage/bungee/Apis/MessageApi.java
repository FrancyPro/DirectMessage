package net.daechler.directmessage.bungee.Apis;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class MessageApi {
    @Getter
    private static Map<UUID, UUID> lastSenderMap = new HashMap<>();
    @Getter
    public static Set<UUID> msgDisabledSet = new HashSet<>();

    public static void sendMessage(ProxiedPlayer sender, ProxiedPlayer receiver, String message) {
        if (msgDisabledSet.contains(receiver.getUniqueId())) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "This player has disabled messages."));
            return;
        }

        String formattedMessage = ChatColor.translateAlternateColorCodes('&',
                "&e&lYou &r&7» &e&l" + receiver.getName() + "&7:&r " + message);
        String formattedMessageForReceiver = ChatColor.translateAlternateColorCodes('&',
                "&e&l" + sender.getName() + " &r&7» &e&lYou&7:&r " + message);

        // Added console logging
        ProxyServer.getInstance().getLogger().info(ChatColor.stripColor("Private Message: " + sender.getName() + " » " + receiver.getName() + ": " + message));

        sender.sendMessage(new TextComponent(formattedMessage));
        receiver.sendMessage(new TextComponent(formattedMessageForReceiver));

        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.hasPermission("directmessage.spy") && !player.equals(sender) && !player.equals(receiver)) {
                player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                        "&4&l[SPY]&r &e&l" + sender.getName() + " &r&7» &e&l" + receiver.getName() + "&7:&r " + message)));
            }
        }

        lastSenderMap.put(receiver.getUniqueId(), sender.getUniqueId());
    }
}
