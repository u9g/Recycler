package com.github.u9g.recycler.data;

import org.bukkit.Material;

public record ItemWithQuantity(Material itemType, int count) {
    public static ItemWithQuantity fromString(String string) {
        String[] str = string.split(":");
        int cnt = 1;
        try { cnt = Integer.parseInt(str[1]); } catch (Exception ignored) {}
        return new ItemWithQuantity(Material.valueOf(str[0]), cnt);
    }

    @Override
    public String toString() {
        return itemType.name() + ":" + count;
    }
}
