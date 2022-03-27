package com.github.u9g.recycler;

import com.github.u9g.u9gutils.NBTUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.misc.Task;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.u9g.recycler.Constants.*;
import static com.github.u9g.recycler.InventoryActions.setupRecycleOutput;

public class RecyclerOpenHandler implements Listener {
    private static final int ANVIL_BUTTON = 13;
    private static final int SLOT_TO_RECYCLE = 10;

    private ItemButton makeBtn(InventoryGUI gui, Inventory playerInv, Location dropItemsLocation) {
        return new ItemButton(Util.make(Material.ANVIL, "Recycle")) {
            @Override
            public void onClick(InventoryClickEvent e) {
                setupRecycleOutput(gui, false);
                gui.closeSlot(SLOT_TO_RECYCLE);
                InventoryActions.removeBtnIfExists(gui, ANVIL_BUTTON);
                var inv = gui.getInventory();
                craftSlot2PrevSlot.values().forEach(slot -> {
                    ItemStack item = inv.getItem(slot);
                    if (item == null) return;
                    if (playerInv.firstEmpty() == -1) dropItemsLocation.getWorld().dropItemNaturally(dropItemsLocation, item);
                    else playerInv.addItem(item);
                    inv.clear(slot);
                });
                inv.clear(SLOT_TO_RECYCLE);
                setupAnvilForUse(gui);
            }
        };
    }

    private boolean recycledItem(Inventory inv) {
        return (Objects.requireNonNull(inv.getItem(0)).getType().equals(Material.RED_STAINED_GLASS_PANE));
    }

    @EventHandler
    public void onPlaceRecycler(BlockPlaceEvent event) {
        var block = event.getBlock();
        var mainHand = event.getPlayer().getEquipment().getItemInMainHand();
        if (
                !block.getType().equals(Material.ANVIL) ||
                        !(mainHand.getItemMeta() != null && NBTUtil.getAsBoolean(mainHand.getItemMeta(), RECYCLER_NSK).orElse(false))
        ) return;
        var key = BlockNSKey.getAnvilNSKeyFor(block);
        NBTUtil.set(block.getChunk(), key, true);
    }

    @EventHandler
    public void onBreakRecycler(BlockBreakEvent event) {
        var block = event.getBlock();
        var key = BlockNSKey.getAnvilNSKeyFor(block);
        NBTUtil.set(block.getChunk(), key, false);
    }

    private void setupAnvilForUse(InventoryGUI gui) {
        Util.applyMask(gui, recyclerMask, glassMask);
        gui.openSlot(SLOT_TO_RECYCLE);
    }

    @EventHandler
    public void onOpenRecycler(PlayerInteractEvent event) {
        if (Objects.equals(event.getHand(), EquipmentSlot.OFF_HAND) || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (!Objects.equals(block.getType(), Material.ANVIL) ||
                !NBTUtil.getAsBoolean(block.getChunk(), BlockNSKey.getAnvilNSKeyFor(block)).orElse(false)) return;

        event.setCancelled(true);

        InventoryGUI gui = new InventoryGUI(Bukkit.createInventory(null, 27, Component.text("Recycler")));
        setupAnvilForUse(gui);
        var playerInv = event.getPlayer().getInventory();
        var dropItemsLocation = event.getClickedBlock().getLocation().add(0, 1, 0);
        gui.setOnClickOpenSlot((e) -> onInventoryClick(gui, playerInv, dropItemsLocation));
        gui.setOnDragOpenSlot((e) -> onInventoryClick(gui, playerInv, dropItemsLocation));
        gui.open(event.getPlayer());
    }

    private void onInventoryClick(InventoryGUI gui, Inventory playerInv, Location dropItemsLocation) {
        Task.syncDelayed(() -> {
            Inventory inv = gui.getInventory();
            if (recycledItem(inv)) return;
            ItemStack item = inv.getItem(SLOT_TO_RECYCLE);
            if (item == null) {
                InventoryActions.removeBtnIfExists(gui, ANVIL_BUTTON);
                Util.applyMask(gui, recyclerMask, glassMask);
                return;
            }
            var recipes = Main.INSTANCE.config.recipes.get(item.getType());
            if (recipes == null || !recipes.enabled()) return;
            craftSlot2PrevSlot.values().forEach(i -> inv.setItem(i, null));
            setupRecycleOutput(gui, true);
            gui.addButton(ANVIL_BUTTON, makeBtn(gui, playerInv, dropItemsLocation));
        });
    }
}
