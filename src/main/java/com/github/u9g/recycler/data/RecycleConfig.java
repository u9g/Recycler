package com.github.u9g.recycler.data;

import com.github.u9g.recycler.Constants;
import com.github.u9g.recycler.Main;
import org.bukkit.Material;
import redempt.redlib.config.ConfigManager;
import redempt.redlib.config.annotations.ConfigMappable;

import java.util.Map;

@ConfigMappable
public class RecycleConfig {
    public Map<Material, ItemRecycleRecipe> recipes;

    public static RecycleConfig init() {
        var plugin = Main.INSTANCE;
        final RecycleConfig recycleConfig = new RecycleConfig();
        recycleConfig.recipes = Map.of(
        Material.IRON_PICKAXE, new ItemRecycleRecipe(false, Map.of(
            0, Material.IRON_INGOT,
            1, Material.IRON_INGOT,
            2, Material.IRON_INGOT,
            4, Material.STICK,
            7, Material.STICK
        )),
        Material.MELON, new ItemRecycleRecipe(true, Map.of(
            0, Material.MELON_SLICE,
            1, Material.MELON_SLICE,
            2, Material.MELON_SLICE,
            3, Material.MELON_SLICE,
            4, Material.MELON_SLICE,
            5, Material.MELON_SLICE,
            6, Material.MELON_SLICE,
            7, Material.MELON_SLICE,
            8, Material.MELON_SLICE
        )));
        makeConfigManager(recycleConfig).saveDefaults().load();
        return recycleConfig;
    }

    private static ConfigManager makeConfigManager(RecycleConfig config) {
        final ConfigManager configManager = ConfigManager.create(Main.INSTANCE, "recycle_config.yml");
        return configManager
                .addConverter(
                        Constants.MATERIAL_CONVERTER, configManager.getConversionManager().getConverter(Constants.MATERIAL_CONVERTER))
                .target(config);
    }

    public void save(Material keyItem, Map<Integer, Material> recipe) {
        recipes.put(keyItem, new ItemRecycleRecipe(true, recipe));
        makeConfigManager(this).save();
    }
}

