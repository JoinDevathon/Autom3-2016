package org.devathon.contest2016;

import org.bukkit.plugin.java.JavaPlugin;

public class DevathonPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Woo! Machines activate!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Beep boop. Shutting down...");
    }
}

