package com.github.u9g.recycler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.inventorygui.InventoryGUI;

import java.util.List;
import java.util.Map;

public class Util {
    public static ItemStack make(Material material, String name) {
        ItemStack item = new ItemStack(material);
        if (!name.equals("")) {
            var im = item.getItemMeta();
            Component c = Component.text(name).colorIfAbsent(NamedTextColor.WHITE);
            if (!c.hasDecoration(TextDecoration.ITALIC)) {
                c = c.decoration(TextDecoration.ITALIC, false);
            }
            im.displayName(c);
            item.setItemMeta(im);
        }
        return item;
    }

    public static void applyMask(InventoryGUI gui, List<String> rows, Map<Character, ItemStack> items) {
        var inv = gui.getInventory();
        for (int y = 0; y < rows.size(); y++) {
            var row = rows.get(y).toCharArray();
            for (int x = 0; x < row.length; x++) {
                char c = row[x];
                if (c == ' ') continue;
                var item = items.get(c);
                if (item == null) continue;
                inv.setItem((y*9)+x, item);
            }
        }
    }
}
