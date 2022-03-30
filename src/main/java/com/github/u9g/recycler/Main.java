package com.github.u9g.recycler;

import com.github.u9g.recycler.data.ItemRecycleRecipe;
import com.github.u9g.recycler.data.RecycleConfig;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.CommandParser;

public final class Main extends JavaPlugin {
    public static Main INSTANCE;
    public RecycleConfig config;
    public Table<Material, Integer, ItemRecycleRecipe> recipeTable = HashBasedTable.create();

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
