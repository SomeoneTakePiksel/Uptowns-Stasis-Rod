package me.piksel.uptownsStasisRod;

import me.piksel.uptownsStasisRod.rod.saveStasisRod;
import me.piksel.uptownsStasisRod.rod.useStasisRod;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class UptownsStasisRod extends JavaPlugin {
    private static UptownsStasisRod instance;
    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        saveDefaultConfig();
        instance = this;
        // Plugin startup logic
        getCommand("stasisRod").setExecutor(new saveStasisRod());
        getServer().getPluginManager().registerEvents(new useStasisRod() , this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static UptownsStasisRod getInstance() {
        return instance;
    }
}
