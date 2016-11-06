package org.devathon.contest2016;

import gnu.trove.map.hash.THashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
    private final Map<Location, CustomBlock> blockActivators = new THashMap<>();

    public BlockManager(DevathonPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean registerBlock(ItemStack itemInHand, Block blockPlaced) {
        final CustomBlock block;
        switch (itemInHand.getItemMeta().getDisplayName()) {
            case "Coffee Grinder":
                block = new CoffeeGrinder(this, blockPlaced);
                break;
            case "Coffee Machine":
                block = new CoffeeMachine(this, blockPlaced);
                break;
            default:
                return true;
        }
        blockLocations.put(blockPlaced.getLocation(), block);
        blockActivators.put(block.getActivatorLocation(), block);
        getConfig().set(locationString(blockPlaced.getLocation()), itemInHand.getItemMeta().getDisplayName());
        saveConfig();
        return false;
    }

    public void unregisterBlock(Block block) {
        final CustomBlock customBlock = blockLocations.remove(block.getLocation());
        if (customBlock == null) {
            return;
        }

        customBlock.destroy();
        getConfig().set(locationString(block.getLocation()), null);
        saveConfig();
    }

    public CustomBlock getBlockForActivator(Location location) {
        return blockActivators.get(location);
    }

    public CustomBlock getCustomBlock(Location location) {
        return blockLocations.get(location);
    }

    public DevathonPlugin getPlugin() {
        return plugin;
    }

    private ConfigurationSection getConfig() {
        final FileConfiguration config = plugin.getDevathonConfig();
        ConfigurationSection section = config.getConfigurationSection("blocks");
        if (section == null) {
            section = config.createSection("blocks");
        }
        return section;
    }

    private void saveConfig() {
        plugin.saveDevathonConfig();
    }

    private String locationString(Location location) {
        return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

}
