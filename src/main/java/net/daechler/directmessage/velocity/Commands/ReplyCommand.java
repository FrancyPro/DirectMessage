package net.daechler.directmessage.velocity.Commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.daechler.directmessage.velocity.Apis.MessageApi;
import net.daechler.directmessage.velocity.DirectMessage;
import net.kyori.adventure.text.Component;

import java.util.Optional;
import java.util.UUID;

public class ReplyCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player) || args.length < 1) {
            source.sendMessage(Component.text("Usage: /r <Message>"));
            return;
        }

        UUID lastSenderId = MessageApi.getLastSenderMap().get(((Player) source).getUniqueId());
        if (lastSenderId == null) {
            source.sendMessage(Component.text("No one to reply to."));
            return;
        }

        Optional<Player> lastSender = DirectMessage.getServer().getPlayer(lastSenderId);
        if (lastSender.isEmpty()) {
            source.sendMessage(Component.text("Player not online."));
            return;
        }

        String message = String.join(" ", args);
        MessageApi.sendMessage((Player) source, lastSender.get(), message);
    }
}
