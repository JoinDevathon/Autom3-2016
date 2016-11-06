package org.devathon.contest2016.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.devathon.contest2016.BlockManager;
import org.devathon.contest2016.recipe.CustomMaterial;

/**
 *
 * @author Autom
 */
public class CoffeeMachine extends CustomBlock {

    private static final int time = 1;
    private final Furnace furnace;
    private final Block relative;
    private boolean active = false;

    public CoffeeMachine(BlockManager manager, Block block) {
        super(manager, block);
        furnace = (Furnace) block.getState();
        //thanks @Scarsz lol#4227  :)
        relative = block.getRelative(((org.bukkit.material.Furnace) furnace.getData()).getFacing());
    }

    @Override
    public void destroy() {

    }

    @Override
    public Location getActivatorLocation() {
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

        if (active || furnace.getInventory().getSmelting() == null || !CustomMaterial.GROUND_COFFEE_BEANS.equals(furnace.getInventory().getSmelting())
                || furnace.getInventory().getResult() != null) {
            return true;
        }
        
        furnace.setBurnTime((short) time);
        furnace.setCookTime((short) time);

        active = true;
        button.setPowered(true);
        state.update(true);

        manager.getPlugin().getServer().getScheduler().runTaskLater(manager.getPlugin(), () -> {
            active = false;

            process();

            button.setPowered(false);
            state.update(true);
        }, time * 20);
        return true;
    }

    public void pass(ItemStack item) {

    }

    private void process() {
        final ItemStack smelting = furnace.getInventory().getSmelting();
        if (smelting == null || !CustomMaterial.GROUND_COFFEE_BEANS.equals(smelting)) {
            return;
        }
        
        final int smeltingAmount = smelting.getAmount();
        if (smeltingAmount == 1) {
            furnace.getInventory().setSmelting(null);
        } else {
            smelting.setAmount(smeltingAmount - 1);
            //furnace.getInventory().setSmelting(smelting);
        }
        
        final ItemStack fuel = furnace.getInventory().getFuel();
        final int fuelAmount = fuel.getAmount();
        if (fuelAmount == 1) {
            furnace.getInventory().setSmelting(null);
        } else {
            fuel.setAmount(fuelAmount - 1);
            //furnace.getInventory().setFuel(fuel);
        }
        
        furnace.getInventory().setResult(CustomMaterial.COFFEE.getItem());
    }

}
