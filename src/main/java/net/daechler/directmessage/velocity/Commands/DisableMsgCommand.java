package net.daechler.directmessage.velocity.Commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.daechler.directmessage.velocity.Apis.MessageApi;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.UUID;

public class DisableMsgCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!(source instanceof Player)) {
            source.sendMessage(Component.text("Only players can disable messages!"));
            return;
        }

        UUID playerId = ((Player) source).getUniqueId();
        if (MessageApi.getMsgDisabledSet().contains(playerId)) {
            MessageApi.getMsgDisabledSet().remove(playerId);
            source.sendMessage(Component.text(NamedTextColor.GREEN+"Messages enabled!"));
        } else {
            MessageApi.getMsgDisabledSet().add(playerId);
            source.sendMessage(Component.text(NamedTextColor.RED+"Messages disabled!"));
        }
    }
}
