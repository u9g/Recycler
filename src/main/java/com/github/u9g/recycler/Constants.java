package com.github.u9g.recycler;

import com.github.u9g.u9gutils.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.config.ConfigType;

import java.util.List;
import java.util.Map;

public class Constants {
    public static final NamespacedKey RECYCLER_NSK = NamespacedKey.fromString("recycler:recycler");
    public static final ItemStack RECYCLER_ITEM = ItemBuilder.of(Material.ANVIL)
            .set(Constants.RECYCLER_NSK, true)
            .name(MiniMessage.miniMessage().deserialize("<gold>Recycler")).build();

    public static final ConfigType<Material> MATERIAL_CONVERTER = new ConfigType<>(Material.class);

    public static Map<Integer, Integer> craftSlot2PrevSlot = Map.of(
      0, 6,
      1, 7,
      2, 8,
      3, 15,
      4, 16,
      5, 17,
      6, 24,
      7, 25,
      8, 26
    );

    public static List<String> recyclerMask = List.of(
            "222111444",
            "2 2111444",
            "222111444");

//    public static List<String> afterRecycleMask = List.of(
//            "444      ",
//            "4 4      ",
//            "444      ");

    public static Map<Character, ItemStack> glassMask =  Map.of(
            '1', Util.make(Material.GRAY_STAINED_GLASS_PANE, " "),
            '3', Util.make(Material.CYAN_STAINED_GLASS_PANE, " "),
            '2', Util.make(Material.GREEN_STAINED_GLASS_PANE, " "),
            '4', Util.make(Material.RED_STAINED_GLASS_PANE, " "));
}
