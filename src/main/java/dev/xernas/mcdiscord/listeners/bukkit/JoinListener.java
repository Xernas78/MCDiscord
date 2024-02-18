package dev.xernas.mcdiscord.listeners.bukkit;

import dev.xernas.mcdiscord.Linker;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!Linker.isLinked(uuid)) {
            String code = genRandomCode();
            Linker.addUserToQueue(uuid, code);
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You need to link your account to join the server !\n" + ChatColor.GREEN + "Use /link " + code + " in discord");
            return;
        }
    }

    private String genRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return stringBuilder.toString();
    }

}
