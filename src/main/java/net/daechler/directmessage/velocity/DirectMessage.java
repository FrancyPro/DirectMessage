/**
 *
 * Velocity Support created by ItzFrancy
 *
 * **/

package net.daechler.directmessage.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.daechler.directmessage.velocity.Apis.CommandApi;
import net.daechler.directmessage.velocity.Commands.DisableMsgCommand;
import net.daechler.directmessage.velocity.Commands.MessageCommand;
import net.daechler.directmessage.velocity.Commands.ReplyCommand;
import org.slf4j.Logger;

@Plugin(
        id = "directmessage",
        name = "DirectMessage",
        version = "1.0",
        description = "A simple direct messaging plugin for Velocity.",
        authors = {"Daechler"}
)
public class DirectMessage {
    @Getter
    private static DirectMessage instance;
    @Getter
    private static ProxyServer server;
    @Getter
    private static Logger logger;

    @Inject
    public DirectMessage(ProxyServer server, Logger logger) {
        instance = this;
        DirectMessage.server = server;
        DirectMessage.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = getServer().getCommandManager();
        CommandApi.registerCommand(this, commandManager, new DisableMsgCommand(), "disablemsg");
        CommandApi.registerCommand(this, commandManager, new MessageCommand(), "msg");
        CommandApi.registerCommand(this, commandManager, new ReplyCommand(), "r");
    }
}
