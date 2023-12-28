package net.daechler.directmessage.velocity.Commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.daechler.directmessage.velocity.Apis.MessageApi;
import net.daechler.directmessage.velocity.DirectMessage;
import net.kyori.adventure.text.Component;

import java.util.Arrays;
import java.util.Optional;

public class MessageCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player) || args.length < 2) {
            source.sendMessage(Component.text("Usage: /msg <Name> <Message>"));
            return;
        }

        Optional<Player> receiver = DirectMessage.getServer().getPlayer(args[0]);
        if (receiver.isEmpty()) {
            source.sendMessage(Component.text("Player not found."));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        MessageApi.sendMessage((Player) source, receiver.get(), message);
    }
}
