package dev.xernas.mcdiscord.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.atomic.AtomicReference;

public class ConfigReader {

    private final FileConfiguration config;

    public ConfigReader(FileConfiguration config) {
        this.config = config;
    }

    public String getToken() {
        return config.getString("bot.token");
    }

    public Long getServerId() {
        long serverId = config.getLong("bot.server-id");
        return serverId == 0 ? null : serverId;
    }

    public String getGroupFromRole(Long roleId) {
        ConfigurationSection roles = config.getConfigurationSection("roles");
        if (roles == null) {
            return null;
        }
        AtomicReference<String> role = new AtomicReference<>("");
        roles.getValues(false).forEach((string, o) -> {
            if (o.equals(roleId)) {
                role.set(string);
            }
        });
        return role.get();
    }

}
