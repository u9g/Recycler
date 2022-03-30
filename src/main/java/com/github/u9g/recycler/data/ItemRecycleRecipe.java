package com.github.u9g.recycler.data;

import org.bukkit.Material;

import java.util.Map;

public record ItemRecycleRecipe(boolean enabled, Map<Integer, ItemWithQuantity> recipe) {
}
