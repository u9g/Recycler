package com.github.u9g.recycler;

import com.github.u9g.recycler.data.RecycleConfig;
import com.github.u9g.u9gutils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.misc.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommandHandler {
    private final int ITEM_SLOT = 10;
    private final int SAVE_SLOT = 13;
    private final ItemStack SAVE_ITEM = ItemBuilder.of(Material.NETHER_STAR).name(Component.text("Save")).build();

    @CommandHook("give")
    public void onCmdGive(Player commandSender) {
        commandSender.getInventory().addItem(Constants.RECYCLER_ITEM);
    }

    @CommandHook("config")
    public void onCmdConfig(Player commandSender) {
        // /recycler:recipesconfig
        var gui = new InventoryGUI(Bukkit.createInventory(null, 27, Component.text("Config editor")));
        Util.applyMask(gui, Constants.recyclerMask, Constants.glassMask);
        gui.open(commandSender);
        gui.openSlot(ITEM_SLOT);
        gui.setOnDragOpenSlot((e) -> onKeyItemChange(gui));
        gui.setOnClickOpenSlot((e) -> onKeyItemChange(gui));
        gui.setReturnsItems(false);
    }

    private void onKeyItemChange(InventoryGUI gui) {
        var tempItem = gui.getInventory().getItem(ITEM_SLOT);
        Task.syncDelayed(() -> {
            // only do something if the ITEM_SLOT changed
            if (Objects.equals(tempItem, gui.getInventory().getItem(ITEM_SLOT))) return;
            var inv = gui.getInventory();
            var item = inv.getItem(ITEM_SLOT);
            if (item != null && item.getAmount() == 1) {
                // clear grid
                Constants.craftSlot2PrevSlot.values().forEach((slot) -> {
                        gui.openSlot(slot);
                        inv.setItem(slot, null);
                });
                var recipes = Main.INSTANCE.config.recipes.get(item.getType());
                if (recipes != null) {
                    recipes.recipe().forEach((slot, material) ->
                            inv.setItem(Constants.craftSlot2PrevSlot.get(slot), ItemBuilder.of(material).build()));
                }
                gui.addButton(SAVE_SLOT, new ItemButton(SAVE_ITEM) {
                    @Override
                    public void onClick(InventoryClickEvent e) {
                        onSave(gui);
                    }
                });
            } else {
                Constants.craftSlot2PrevSlot.values().forEach(gui::closeSlot);
                InventoryActions.removeBtnIfExists(gui, SAVE_SLOT);
                Util.applyMask(gui, Constants.recyclerMask, Constants.glassMask);
            }
        });
    }

    public void onSave(InventoryGUI gui) {
        Inventory inv = gui.getInventory();
        Map<Integer, Material> materials = new HashMap<>();
        int i = -1;
        for (int value : Constants.craftSlot2PrevSlot.values().stream().sorted().toList()) {
            i++;
            var item = inv.getItem(value);
            if (item == null) continue;
            materials.put(i, item.getType());
        }
        var key = inv.getItem(ITEM_SLOT);
        Objects.requireNonNull(key);
        // refund all items
        var item = inv.getItem(ITEM_SLOT);
        Objects.requireNonNull(item);
        var playerInv = inv.getViewers().get(0).getInventory();
        playerInv.addItem(item);
        for (int slot : Constants.craftSlot2PrevSlot.values()) {
            var craftSlotItem = inv.getItem(slot);
            if (craftSlotItem == null) continue;
            playerInv.addItem(craftSlotItem);
        }
        Main.INSTANCE.config.save(Objects.requireNonNull(inv.getItem(ITEM_SLOT)).getType(), materials);
        // hack to reset gui after save
        onKeyItemChange(gui);
        gui.getInventory().clear(ITEM_SLOT);
    }
}
