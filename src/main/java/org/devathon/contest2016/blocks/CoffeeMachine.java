package org.devathon.contest2016.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.devathon.contest2016.BlockManager;

/**
 *
 * @author Autom
 */
public class CoffeeMachine extends CustomBlock {

    private final Furnace furnace;
    private final Block relative;
    private boolean active = false;

    public CoffeeMachine(BlockManager manager, Block block) {
        super(manager, block);
        furnace = (Furnace) block.getState();
        relative = block.getRelative(((org.bukkit.material.Furnace) furnace.getData()).getFacing());
    }

    @Override
    public void destroy() {

    }

    @Override
    public Location getActivatorLocation() {
        //thanks scarsz :)
        return relative.getLocation();
    }

    @Override
    public boolean activate() {
        final BlockState state = relative.getState();
        if (relative.getType() != Material.STONE_BUTTON) {
            return false;
        }

        final Button button = (Button) state.getData();
        if (button.getFacing() != ((org.bukkit.material.Furnace) furnace.getData()).getFacing()) {
            return false;
        }

        if (active) {
            return true;
        }

        active = true;
        button.setPowered(true);
        state.update(true);

        manager.getPlugin().getServer().getScheduler().runTaskLater(manager.getPlugin(), () -> {
            active = false;

            process();

            button.setPowered(false);
            state.update(true);
        }, 20);
        return true;
    }

    public void pass(ItemStack item) {

    }

    private void process() {
        
    }

}
