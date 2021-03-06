package org.devathon.contest2016.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.bukkit.scheduler.BukkitTask;
import org.devathon.contest2016.BlockManager;
import org.devathon.contest2016.recipe.CustomMaterial;

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
    public boolean activate(Player player, ItemStack item) {
        if (CustomMaterial.MUG.equals(item)) {
            final int amount = item.getAmount();
            if (amount == 1) {
                player.getInventory().setItemInMainHand(null);
                player.updateInventory();
            } else {
                item.setAmount(amount - 1);
            }
            ItemStack fuel = furnace.getInventory().getFuel();
            if (fuel == null) {
                fuel = item.clone();
                fuel.setAmount(1);
            } else {
                fuel.setAmount(fuel.getAmount() + 1);
            }
            furnace.getInventory().setFuel(fuel);
        }

        final BlockState state = relative.getState();
        if (relative.getType() != Material.STONE_BUTTON) {
            return false;
        }

        final Button button = (Button) state.getData();
        if (button.getFacing() != ((org.bukkit.material.Furnace) furnace.getData()).getFacing()) {
            return false;
        }

        if (active || furnace.getInventory().getSmelting() == null || !CustomMaterial.GROUND_COFFEE_BEANS.equals(furnace.getInventory().getSmelting())
                || !CustomMaterial.MUG.equals(furnace.getInventory().getFuel()) || furnace.getInventory().getResult() != null) {
            return true;
        }

        furnace.setBurnTime((short) (getConfig().getInt("time")));

        active = true;
        button.setPowered(true);
        state.update(true);

        BukkitTask task = manager.getPlugin().getServer().getScheduler().runTaskTimer(manager.getPlugin(), () -> {
            player.getWorld().playSound(block.getLocation(), Sound.BLOCK_LAVA_POP, 0.2f, 0.5f);
        }, 0, 1);
        manager.getPlugin().getServer().getScheduler().runTaskLater(manager.getPlugin(), () -> {
            active = false;

            process();

            button.setPowered(false);
            state.update(true);
            task.cancel();
            player.stopSound(Sound.BLOCK_LAVA_POP);
            player.getNearbyEntities(50, 50, 50).stream().filter(entity -> entity.getType() == EntityType.PLAYER)
                    .forEach(entity -> ((Player) entity).stopSound(Sound.BLOCK_LAVA_POP));
        }, getConfig().getInt("time"));
        return true;
    }

    @Override
    public boolean inventoryClick(InventoryClickEvent event) {
        if (!(event.getClickedInventory() instanceof FurnaceInventory)) {
            return false;
        }

        if (event.getSlotType() == SlotType.CRAFTING && !CustomMaterial.GROUND_COFFEE_BEANS.equals(event.getCursor())) {
            return true;
        }

        if (event.getSlotType() == SlotType.FUEL && !CustomMaterial.MUG.equals(event.getCurrentItem())) {
            return true;
        }

        return false;
    }

    public boolean pass(ItemStack item) {
        if (!CustomMaterial.GROUND_COFFEE_BEANS.equals(item)) {
            return false;
        }

        ItemStack smelting = furnace.getInventory().getSmelting();
        if (smelting == null) {
            furnace.getInventory().setSmelting(item);
            return true;
        }

        final int amount = smelting.getAmount();
        if (amount == smelting.getMaxStackSize()) {
            return false;
        }

        smelting.setAmount(amount + 1);
        return true;
    }

    private void process() {
        final ItemStack smelting = furnace.getInventory().getSmelting();
        if (smelting == null || !CustomMaterial.GROUND_COFFEE_BEANS.equals(smelting) || !CustomMaterial.MUG.equals(furnace.getInventory().getFuel())) {
            return;
        }

        final int smeltingAmount = smelting.getAmount();
        if (smeltingAmount == 1) {
            furnace.getInventory().setSmelting(null);
        } else {
            smelting.setAmount(smeltingAmount - 1);
        }

        final ItemStack fuel = furnace.getInventory().getFuel();
        final int fuelAmount = fuel.getAmount();
        if (fuelAmount == 1) {
            furnace.getInventory().setFuel(null);
        } else {
            fuel.setAmount(fuelAmount - 1);
        }

        furnace.getInventory().setResult(CustomMaterial.COFFEE.getItem());
    }

    private ConfigurationSection getConfig() {
        return manager.getPlugin().getConfig().getConfigurationSection("machine");
    }

}
