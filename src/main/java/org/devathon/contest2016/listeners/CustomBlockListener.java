package org.devathon.contest2016.listeners;

import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.devathon.contest2016.DevathonPlugin;

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

}
