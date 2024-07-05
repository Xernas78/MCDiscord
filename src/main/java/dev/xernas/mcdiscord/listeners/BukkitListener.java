package dev.xernas.mcdiscord.listeners;

import dev.xernas.mcdiscord.Linker;
import dev.xernas.mcdiscord.config.ConfigReader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitListener implements Listener {

    private final Guild guild;
    private final ConfigReader configReader;
    private final Permission vaultPermission;

    public BukkitListener(Guild guild, ConfigReader configReader, Permission vaultPermission) {
        this.guild = guild;
        this.configReader = configReader;
        this.vaultPermission = vaultPermission;
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!Linker.isLinked(uuid)) {
            String code = genRandomCode();
            Linker.addUserToQueue(uuid, code);
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You need to link your account to join the server !\n" + ChatColor.GREEN + "Use /link " + code + " in discord");
            return;
        }
        Long userId = Linker.getLinkedUser(uuid);
        Member member = guild.retrieveMemberById(userId).complete();
        if (member == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You need to be in the discord server to join the server !\n" + ChatColor.GREEN + "Join the discord server and link your account to join the server");
            return;
        }
        List<Role> roles = member.getRoles();
        List<Long> rolesId = new ArrayList<>();
        roles.forEach(role -> rolesId.add(role.getIdLong()));
        for (Long roleId : rolesId) {
            String group = configReader.getGroupFromRole(roleId);
            if (group != null) {
                if (!vaultPermission.playerInGroup(event.getPlayer(), group)) {
                    vaultPermission.playerAddGroup(event.getPlayer(), group);
                }
            }
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
