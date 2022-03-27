package com.github.u9g.recycler;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.inventorygui.InventoryGUI;

public class InventoryActions {
    public static void setupRecycleOutput(InventoryGUI gui, boolean makingPreview) {
        Inventory inv = gui.getInventory();
        for (var slot : Constants.craftSlot2PrevSlot.values()) {
            inv.setItem(slot, null);
        }
        ItemStack item = inv.getItem(10);
        var output = Main.INSTANCE.config.recipes.get(item.getType());
        output.recipe().forEach((rawSlot, mat) ->
                inv.setItem(
                        Constants.craftSlot2PrevSlot.get(rawSlot),
                        Util.make(mat, makingPreview ? "Press anvil to recycle to components" : "").asQuantity(item.getAmount())
                )
        );
    }

    public static void removeBtnIfExists(InventoryGUI gui, int slot) {
        var btn = gui.getButton(slot);
        if (btn == null) return;
        gui.removeButton(btn);
    }
}
