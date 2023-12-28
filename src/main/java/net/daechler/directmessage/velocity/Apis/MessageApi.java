package net.daechler.directmessage.velocity.Apis;

import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import net.daechler.directmessage.velocity.DirectMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;

public class MessageApi {
    @Getter
    private static Map<UUID, UUID> lastSenderMap = new HashMap<>();
    @Getter
    public static Set<UUID> msgDisabledSet = new HashSet<>();

    public static void sendMessage(Player sender, Player receiver, String message) {
        if (msgDisabledSet.contains(receiver.getUniqueId())) {
            sender.sendMessage(Component.text("§cThis player has disabled messages."));
            return;
        }

        Component formattedMessage = LegacyComponentSerializer.legacy('&').deserialize("&e&lYou &r&7» &e&l" + receiver.getUsername() + "&7:&r " + message);
        Component formattedMessageForReceiver = LegacyComponentSerializer.legacy('&').deserialize("&e&l" + sender.getUsername() + " &r&7» &e&lYou&7:&r " + message);

        // Added console logging
        DirectMessage.getLogger().info("Private Message: " + sender.getUsername() + " » " + receiver.getUsername() + ": " + message);

        sender.sendMessage(formattedMessage);
        receiver.sendMessage(formattedMessageForReceiver);

        for (Player player : DirectMessage.getServer().getAllPlayers()) {
            if (player.hasPermission("directmessage.spy") && !player.equals(sender) && !player.equals(receiver)) {
                player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize("&4&l[SPY]&r &e&l" + sender.getUsername() + " &r&7» &e&l" + receiver.getUsername() + "&7:&r " + message));
            }
        }

        lastSenderMap.put(receiver.getUniqueId(), sender.getUniqueId());
    }
}
