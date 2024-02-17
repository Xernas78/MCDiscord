package dev.xernas.mcdiscord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCDiscord extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            JDA jda = JDABuilder.createDefault("MTIwNzA0ODUxOTg2NTU5Nzk5NA.GlGt0-.tLgQfeOZ5expLAKKh9a0QIsU8hn2K37sua390A").build();
        });

    }

    @Override
    public void onDisable() {

    }
}
