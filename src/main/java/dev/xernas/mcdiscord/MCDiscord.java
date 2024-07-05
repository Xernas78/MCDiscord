package dev.xernas.mcdiscord;

import dev.xernas.mcdiscord.config.ConfigReader;
import dev.xernas.mcdiscord.listeners.BukkitListener;
import dev.xernas.mcdiscord.listeners.JDAListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicReference;

public final class MCDiscord extends JavaPlugin {

    private static ConfigReader configReader;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configReader = new ConfigReader(getConfig());
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            JDA jda = JDABuilder.createDefault(configReader.getToken()).build();
            jda.addEventListener(new JDAListener(this, configReader));
        });
    }

    public static Permission setupPermissions(JavaPlugin plugin) {
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        if (vault == null) {
            plugin.getLogger().severe("Cannot find Vault, please install Vault and restart the server");
            return null;
        }
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            plugin.getLogger().severe("Cannot find a permission plugin, please install a permission plugin and restart the server");
            return null;
        }
        return rsp.getProvider();
    }

}
