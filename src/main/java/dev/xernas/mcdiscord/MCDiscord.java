package dev.xernas.mcdiscord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCDiscord extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            JDA jda = JDABuilder.createDefault("").build();
        });

    }

    @Override
    public void onDisable() {

    }
}
