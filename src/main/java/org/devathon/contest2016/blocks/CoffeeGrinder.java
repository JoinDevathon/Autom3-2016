package org.devathon.contest2016.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Lever;
import org.devathon.contest2016.BlockManager;
import org.devathon.contest2016.recipe.CustomMaterial;

/**
 *
 * @author Autom
 */
public class CoffeeGrinder extends CustomBlock {

    private static final int time = 1;
    private final Hopper hopper;
    private boolean active = false;

    public CoffeeGrinder(BlockManager manager, Block block) {
        super(manager, block);
        hopper = (Hopper) block.getState();
    }

    @Override
    public void destroy() {

    }

    @Override
    public Location getActivatorLocation() {
        return block.getRelative(BlockFace.UP).getLocation();
    }

    @Override
    public boolean activate(Player player, ItemStack unused) {
        final BlockState state = block.getRelative(BlockFace.UP).getState();

        if (block.getRelative(BlockFace.UP).getType() != Material.LEVER) {
            return false;
        }

        final Lever lever = (Lever) state.getData();
        if (lever.getFacing() != BlockFace.UP) {
            return false;
        }

        if (active) {
            return true;
        }

        active = true;
        lever.setPowered(true);
        state.update(true);

        for (Item item : block.getWorld().getEntitiesByClass(Item.class)) {
            if (item.getLocation().getBlockX() != block.getLocation().getBlockX()
                    || item.getLocation().getBlockY() != block.getLocation().getBlockY()
                    || item.getLocation().getBlockZ() != block.getLocation().getBlockZ()) {
                continue;
            }

            if (!CustomMaterial.COFFEE_BEAN.equals(item.getItemStack())) {
                continue;
            }

            final ItemStack clone = item.getItemStack().clone();
            clone.setAmount(1);
            hopper.getInventory().addItem(clone);
            final int amount = item.getItemStack().getAmount();
            if (amount == 1) {
                item.remove();
            } else {
                item.getItemStack().setAmount(amount - 1);
            }

            break;
        }

        manager.getPlugin().getServer().getScheduler().runTaskLater(manager.getPlugin(), () -> {
            active = false;

            process();

            lever.setPowered(false);
            state.update(true);
        }, time * 20);
        return true;
    }

    @Override
    public boolean inventoryClick(InventoryClickEvent event) {
        return true;
    }

    private void process() {
        final ItemStack bean = hopper.getInventory().getItem(0);
        if (!CustomMaterial.COFFEE_BEAN.equals(bean)) {
            return;
        }

        final ItemStack item = CustomMaterial.GROUND_COFFEE_BEANS.getItem();
        hopper.getInventory().remove(bean);

        final CustomBlock customBlock = manager.getCustomBlock(hopper.getBlock().getRelative(BlockFace.DOWN).getLocation());
        if (customBlock != null && customBlock instanceof CoffeeMachine) {
            CoffeeMachine coffeeMachine = (CoffeeMachine) customBlock;
            if (coffeeMachine.pass(item)) {
                return;
            }
        }
        
        hopper.getWorld().dropItem(hopper.getLocation().clone().add(0.5, -0.5, 0.5), item);
    }

}
