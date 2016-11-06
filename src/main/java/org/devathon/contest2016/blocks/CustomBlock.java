package org.devathon.contest2016.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.devathon.contest2016.BlockManager;

/**
 *
 * @author Autom
 */
public abstract class CustomBlock {
    
    protected final BlockManager manager;
    protected final Block block;

    public CustomBlock(BlockManager manager, Block block) {
        this.manager = manager;
        this.block = block;
    }
    
    public abstract void destroy();

    public abstract Location getActivatorLocation();

    public abstract boolean activate(Player player, ItemStack item);

    public abstract boolean inventoryClick(InventoryClickEvent event);

}
