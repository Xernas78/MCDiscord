package dev.xernas.mcdiscord;

import dev.xernas.mcdiscord.config.ConfigReader;
import dev.xernas.mcdiscord.listeners.bukkit.JoinListener;
import dev.xernas.mcdiscord.listeners.jda.GuildMessageSlashListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MCDiscord extends JavaPlugin {

    private static ConfigReader configReader;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configReader = new ConfigReader(getConfig());
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            JDA jda = JDABuilder.createDefault(configReader.getToken()).build();
            try {
                jda.awaitReady();
                Long serverId = configReader.getServerId();
                if (serverId == null) {
                    getLogger().severe("Server ID is not set in the config file");
                    jda.shutdownNow();
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
                Guild guild = jda.getGuildById(serverId);
                if (guild == null) {
                    getLogger().severe("Server ID is invalid, cannot find the server with the given ID (" + serverId + ")");
                    jda.shutdownNow();
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
                guild.upsertCommand(
                        Commands.slash("link", "Links your Minecraft account to your discord account")
                                .addOption(OptionType.STRING, "code", "the code to enter")
                ).queue();
                jda.addEventListener(new GuildMessageSlashListener());
            } catch (InterruptedException e) {
                getLogger().severe("An error occurred while trying to connect to discord, please contact support");
                Bukkit.getPluginManager().disablePlugin(this);
            }

        });
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
    }

}
