package dev.xernas.mcdiscord.listeners;

import dev.xernas.mcdiscord.Linker;
import dev.xernas.mcdiscord.MCDiscord;
import dev.xernas.mcdiscord.config.ConfigReader;
import dev.xernas.mcdiscord.utils.MinecraftUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class JDAListener extends ListenerAdapter {

    private final JavaPlugin plugin;
    private final ConfigReader configReader;

    public JDAListener(JavaPlugin plugin, ConfigReader configReader) {
            this.plugin = plugin;
            this.configReader = configReader;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("link")) {
            OptionMapping codeOption = event.getOption("code");
            if (codeOption == null) {
                event.reply("You need to enter a code to link your account").setEphemeral(true).queue();
                return;
            }
            UUID linkedUUID = Linker.link(event.getUser().getIdLong(), codeOption.getAsString());
            if (linkedUUID == null) {
                event.reply("Invalid code").setEphemeral(true).queue();
                return;
            }
            try {
                event.reply("Your discord account is now linked to: " + MinecraftUtils.getUsernameByUUID(linkedUUID)).setEphemeral(true).queue();
            } catch (IOException | InterruptedException e) {
                event.reply("An error occurred while trying to link your account").setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Long serverId = configReader.getServerId();
        if (serverId == null) {
            plugin.getLogger().severe("Server ID is not set in the config file");
            event.getJDA().shutdownNow();
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        Guild guild = event.getJDA().getGuildById(serverId);
        if (guild == null) {
            plugin.getLogger().severe("Server ID is invalid, cannot find the server with the given ID (" + serverId + ")");
            event.getJDA().shutdownNow();
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        Permission vaultPermission = MCDiscord.setupPermissions(plugin);
        if (vaultPermission == null) {
            event.getJDA().shutdownNow();
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        guild.upsertCommand(
                Commands.slash("link", "Links your Minecraft account to your discord account")
                        .addOption(OptionType.STRING, "code", "the code to enter")
        ).queue();
        Bukkit.getPluginManager().registerEvents(new BukkitListener(guild, configReader, vaultPermission), plugin);
    }

    @Override
    public void onException(@NotNull ExceptionEvent event) {
        if (configReader.isJDAErrorsEnabled()) {
            plugin.getLogger().warning("An error occurred with JDA: " + event.getCause().getMessage());
            plugin.getLogger().warning("These errors can appear pretty often and are caused by discord's API rate limiting. If you want to disable this message, you can do so in the config file");
        }
    }
}
