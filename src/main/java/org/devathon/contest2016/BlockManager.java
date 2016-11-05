package org.devathon.contest2016;

import gnu.trove.map.hash.THashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.devathon.contest2016.blocks.CoffeeGrinder;
import org.devathon.contest2016.blocks.CoffeeMachine;
import org.devathon.contest2016.blocks.CustomBlock;

/**
 *
 * @author Autom
 */
public class BlockManager {
    
    private final DevathonPlugin plugin;
    private final Map<Location, CustomBlock> blockLocations = new THashMap<>();

    public BlockManager(DevathonPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean registerBlock(ItemStack itemInHand, Block blockPlaced) {
        final CustomBlock block;
        switch (itemInHand.getItemMeta().getDisplayName()) {
            case "Coffee Grinder":
                block = new CoffeeGrinder(blockPlaced);
                break;
            case "Coffee Machine":
                block = new CoffeeMachine(blockPlaced);
                break;
            default:
                return true;
        }
        blockLocations.put(blockPlaced.getLocation(), block);
        getConfig().set(locationString(blockPlaced.getLocation()), itemInHand.getItemMeta().getDisplayName());
        return false;
    }
    
    private ConfigurationSection getConfig() {
        return plugin.getDevathonConfig().getConfigurationSection("blocks");
    }

    private String locationString(Location location) {
        return location.getWorld()+ ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

}
