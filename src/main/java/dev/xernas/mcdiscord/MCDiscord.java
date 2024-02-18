package dev.xernas.mcdiscord;

import dev.xernas.mcdiscord.config.ConfigReader;
import dev.xernas.mcdiscord.listeners.jda.PrivateMessageSlashListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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
            Objects.requireNonNull(jda.getGuildById(configReader.getServerId())).upsertCommand(
                    Commands.slash("link", "Links your Minecraft account to your discord account")
                            .addOption(OptionType.STRING, "code", "the code to enter")
            ).queue();
            jda.addEventListener(new PrivateMessageSlashListener());
        });
    }

}
