package org.devathon.contest2016;

import gnu.trove.map.hash.THashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
        final ConfigurationSection config = getConfig();
        for (String key : config.getKeys(false)) {
            String string = config.getString(key + ".name");
            String[] split = key.split(";");
            Block block = plugin.getServer().getWorld(split[0]).getBlockAt(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
            block.setType(Material.valueOf(config.getString(key + ".type")));
            block.setData(Byte.parseByte(config.getString(key + ".data")));
            registerBlock(string, block, false);
        }
    }

    public final boolean registerBlock(String name, Block blockPlaced, boolean saveConfig) {
        final CustomBlock block;
        switch (name) {
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
        if (saveConfig) {
            final ConfigurationSection config = getConfig();
            config.set(locationString(blockPlaced.getLocation()) + ".name", name);
            config.set(locationString(blockPlaced.getLocation()) + ".type", blockPlaced.getType().toString());
            config.set(locationString(blockPlaced.getLocation()) + ".data", blockPlaced.getData());
            saveConfig();
        }
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

    public void disable() {
        for (Location location : blockLocations.keySet()) {
            location.getBlock().setType(Material.BARRIER);
        }
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
