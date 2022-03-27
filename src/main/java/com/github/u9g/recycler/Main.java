package com.github.u9g.recycler;

import com.github.u9g.recycler.data.RecycleConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.CommandParser;

public final class Main extends JavaPlugin {
    public static Main INSTANCE;
    public RecycleConfig config;

    @Override
    public void onEnable() {
        INSTANCE = this;
        new CommandParser(this.getResource("commands.rdcml")).parse().register("recycler", new CommandHandler());
        config = RecycleConfig.init();
        Bukkit.getPluginManager().registerEvents(new RecyclerOpenHandler(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
