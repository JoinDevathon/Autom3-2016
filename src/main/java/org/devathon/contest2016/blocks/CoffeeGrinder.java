package org.devathon.contest2016.blocks;

import net.minecraft.server.v1_10_R1.Blocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.material.Lever;
import org.devathon.contest2016.BlockManager;
import org.devathon.contest2016.recipe.CustomMaterial;

/**
 *
 * @author Autom
 */
public class CoffeeGrinder extends CustomBlock {
    
    private boolean active = false;

    public CoffeeGrinder(BlockManager manager, Block block) {
        super(manager, block);
    }

    @Override
    public void destroy() {
        
    }

    @Override
    public Location getActivatorLocation() {
        return block.getRelative(BlockFace.UP).getLocation();
    }

    @Override
    public boolean activate() {
        if (block.getRelative(BlockFace.UP).getType() != Material.LEVER || ((Lever) block.getRelative(BlockFace.UP).getState().getData()).getFacing() != BlockFace.UP) {
            return false;
        }
        
        if (active) {
            return true;
        }
        
        active = true;
        ((Lever) block.getRelative(BlockFace.UP).getState().getData()).setPowered(true);
        manager.getPlugin().getServer().getScheduler().runTaskLater(manager.getPlugin(), () -> {
            active = false;
            ((Lever) block.getRelative(BlockFace.UP).getState().getData()).setPowered(false);
        }, 10);
        return false;
    }

    public void pickup(Item item) {
        if (!active || item == null || !CustomMaterial.COFFEE_BEAN.equals(item.getItemStack())) {
            return;
        }
        
        item.getItemStack().setAmount(item.getItemStack().getAmount() - 1);
        
    }

}
