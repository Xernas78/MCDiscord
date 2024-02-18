package dev.xernas.mcdiscord.listeners.jda;

import dev.xernas.mcdiscord.Linker;
import dev.xernas.mcdiscord.utils.MinecraftUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class GuildMessageSlashListener extends ListenerAdapter {

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
}
