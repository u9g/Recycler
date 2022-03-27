package com.github.u9g.recycler;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockNSKey {
    private static Map<Block, NamespacedKey> block2NSKey = new HashMap<>();
    public static NamespacedKey getAnvilNSKeyFor(Block block) {
        if (block2NSKey.containsKey(block)) return block2NSKey.get(block);
        var location = block.getLocation();
        var chunk = location.getChunk();
        var lowestX = chunk.getX() & 15; // REALLY SHOULD BE: location.getBlockX() & 15; // same as % 16
        var lowestZ = chunk.getZ() & 15; // REALLY SHOULD BE: location.getBlockZ() & 15; // same as % 16
        var relX = location.getX() - lowestX;
        var relZ = location.getZ() - lowestZ;
        block2NSKey.put(block, NamespacedKey.fromString("recycler:isrecycler-" + relX + "-" + location.getBlockY() + "-" + relZ));
        return getAnvilNSKeyFor(block);
    }
}
