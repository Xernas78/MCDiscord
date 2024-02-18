package dev.xernas.mcdiscord.listeners.jda;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PrivateMessageSlashListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("link")) {
            event.reply("Your discord account is now linked to: " + Objects.requireNonNull(event.getOption("code")).getAsString()).setEphemeral(true).queue();
        }
    }
}
