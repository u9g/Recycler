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
        var output = Main.INSTANCE.recipeTable.get(item.getType(), item.getAmount());
        output.recipe().forEach((rawSlot, mat) ->
                inv.setItem(
                        Constants.craftSlot2PrevSlot.get(rawSlot),
                        Util.make(mat.itemType(), makingPreview ? "Press anvil to recycle to components" : "").asQuantity(mat.count())
                )
        );
    }

    public static void removeBtnIfExists(InventoryGUI gui, int slot) {
        var btn = gui.getButton(slot);
        if (btn == null) return;
        gui.removeButton(btn);
    }
}
