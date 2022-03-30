package com.github.u9g.recycler.data;

import com.github.u9g.recycler.Constants;
import com.github.u9g.recycler.Main;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.config.ConfigManager;
import redempt.redlib.config.annotations.ConfigMappable;
import redempt.redlib.config.annotations.ConfigPostInit;

import java.util.HashMap;
import java.util.Map;

@ConfigMappable
public class RecycleConfig {
    public Map<ItemWithQuantity, ItemRecycleRecipe> recipes;

    public static RecycleConfig init() {
        final RecycleConfig recycleConfig = new RecycleConfig();
        recycleConfig.recipes = Map.of(
        new ItemWithQuantity(Material.IRON_PICKAXE, 1), new ItemRecycleRecipe(false, Map.of(
            0, new ItemWithQuantity(Material.IRON_INGOT, 1),
            1, new ItemWithQuantity(Material.IRON_INGOT, 1),
            2, new ItemWithQuantity(Material.IRON_INGOT, 1),
            4, new ItemWithQuantity(Material.STICK, 1),
            7, new ItemWithQuantity(Material.STICK, 1)
        )),
        new ItemWithQuantity(Material.MELON, 1), new ItemRecycleRecipe(true, Map.of(
            0, new ItemWithQuantity(Material.MELON_SLICE, 1),
            1, new ItemWithQuantity(Material.MELON_SLICE, 1),
            2, new ItemWithQuantity(Material.MELON_SLICE, 1),
            3, new ItemWithQuantity(Material.MELON_SLICE, 1),
            4, new ItemWithQuantity(Material.MELON_SLICE, 1),
            5, new ItemWithQuantity(Material.MELON_SLICE, 1),
            6, new ItemWithQuantity(Material.MELON_SLICE, 1),
            7, new ItemWithQuantity(Material.MELON_SLICE, 1),
            8, new ItemWithQuantity(Material.MELON_SLICE, 1)
        )));
        makeConfigManager(recycleConfig).saveDefaults().load();
        return recycleConfig;
    }

    private static ConfigManager makeConfigManager(RecycleConfig config) {
        final ConfigManager configManager = ConfigManager.create(Main.INSTANCE, "recycle_config.yml");
        return configManager
                .addConverter(
                        Constants.MATERIAL_CONVERTER, configManager.getConversionManager().getConverter(Constants.MATERIAL_CONVERTER))
                .addConverter(ItemWithQuantity.class, ItemWithQuantity::fromString, ItemWithQuantity::toString)
                .target(config);
    }

    public void save(ItemWithQuantity keyItem, Map<Integer, ItemWithQuantity> recipe) {
        recipes.put(keyItem, new ItemRecycleRecipe(true, recipe));
        Main.INSTANCE.recipeTable.put(keyItem.itemType(), keyItem.count(), new ItemRecycleRecipe(true, recipe));
        makeConfigManager(this).save();
    }

    @ConfigPostInit
    public void afterInit() {
        recipes.forEach((k, v) -> {
            Main.INSTANCE.recipeTable.put(k.itemType(), k.count(), v);
        });
    }
}

