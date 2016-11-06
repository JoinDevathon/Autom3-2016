package org.devathon.contest2016.listeners;

import java.util.List;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.devathon.contest2016.DevathonPlugin;
import org.devathon.contest2016.blocks.CoffeeGrinder;
import org.devathon.contest2016.blocks.CustomBlock;
import org.devathon.contest2016.recipe.CustomMaterial;

/**
 *
 * @author Autom
 */
public class CustomBlockListener implements Listener {

    private final DevathonPlugin plugin;

    public CustomBlockListener(DevathonPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getItemInHand() == null || event.getItemInHand().getItemMeta() == null) {
            return;
        }
        final List<String> lore = event.getItemInHand().getItemMeta().getLore();
        if ((lore == null || lore.isEmpty()) || !lore.get(0).equals(DevathonPlugin.NAME)) {
            return;
        }

        event.setCancelled(plugin.getBlockManager().registerBlock(event.getItemInHand(), event.getBlockPlaced()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        plugin.getBlockManager().unregisterBlock(event.getBlock());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        CustomBlock block = plugin.getBlockManager().getBlockForActivator(event.getClickedBlock().getLocation());
        if (block == null) {
            return;
        }

        event.setCancelled(block.activate());
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (!(event.getInventory().getHolder() instanceof Hopper)) {
            return;
        }

        Hopper hopper = (Hopper) event.getInventory().getHolder();
        CustomBlock customBlock = plugin.getBlockManager().getCustomBlock(hopper.getBlock().getLocation());
        if (customBlock == null || !(customBlock instanceof CoffeeGrinder)) {
            return;
        }
        
        CoffeeGrinder coffeeGrinder = (CoffeeGrinder) customBlock;
        coffeeGrinder.pickup(event.getItem());
        event.setCancelled(true);
    }

}
