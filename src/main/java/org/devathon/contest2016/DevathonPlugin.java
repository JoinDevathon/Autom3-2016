package org.devathon.contest2016;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.devathon.contest2016.recipe.CustomMaterial;
import org.devathon.contest2016.listeners.CraftingListener;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.listeners.CustomBlockListener;
import org.devathon.contest2016.listeners.CustomItemListener;
import org.devathon.contest2016.recipe.CustomShapedRecipe;
import org.devathon.contest2016.recipe.CustomShapelessRecipe;

public class DevathonPlugin extends JavaPlugin {

    public static final String NAME = ChatColor.DARK_AQUA + "Devathon item";
    private CraftingListener craftingListener;
    private BlockManager blockManager;
    private FileConfiguration devathonConfig = null;
    private File devathonConfigFile = null;

    @Override
    public void onEnable() {
        //saveDefaultConfig();
        saveDefaultDevathonConfig();
        
        craftingListener = new CraftingListener();
        getServer().getPluginManager().registerEvents(craftingListener, this);
        getServer().getPluginManager().registerEvents(new CustomBlockListener(this), this);
        getServer().getPluginManager().registerEvents(new CustomItemListener(this), this);
        blockManager = new BlockManager(this);
        registerCraftingRecipes();
        getLogger().info("Woo! Machines activate!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Beep boop. Shutting down...");
        getServer().resetRecipes();
        blockManager.disable();
        devathonConfig = null;
        devathonConfigFile = null;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }
    
    public void reloadDevathonConfig() {
        if (devathonConfigFile == null) {
            devathonConfigFile = new File(getDataFolder(), "devathon.yml");
        }
        devathonConfig = YamlConfiguration.loadConfiguration(devathonConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(this.getResource("devathon.yml"), "UTF8");
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            devathonConfig.setDefaults(defConfig);
        } catch (UnsupportedEncodingException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        }
    }

    public FileConfiguration getDevathonConfig() {
        if (devathonConfig == null) {
            reloadDevathonConfig();
        }
        return devathonConfig;
    }

    public void saveDevathonConfig() {
        if (devathonConfig == null || devathonConfigFile == null) {
            return;
        }
        try {
            getDevathonConfig().save(devathonConfigFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + devathonConfigFile, ex);
        }
    }

    private void saveDefaultDevathonConfig() {
        if (devathonConfigFile == null) {
            devathonConfigFile = new File(getDataFolder(), "devathon.yml");
        }
        if (!devathonConfigFile.exists()) {
            saveResource("devathon.yml", false);
        }
    }

    private void registerCraftingRecipes() {
        craftingListener.registerCraftingRecipe(new ShapelessRecipe(CustomMaterial.COFFEE_BEAN.getItem()).addIngredient(Material.INK_SACK, 3).addIngredient(Material.SUGAR));
        craftingListener.registerCraftingRecipe(new CustomShapedRecipe(CustomMaterial.COFFEE_GRINDER.getItem(),
                item(Material.COBBLESTONE), item(Material.HOPPER), item(Material.COBBLESTONE),
                item(Material.COBBLESTONE), CustomMaterial.COFFEE_BEAN.getItem(), item(Material.COBBLESTONE),
                item(Material.COBBLESTONE), item(Material.CAULDRON_ITEM), item(Material.COBBLESTONE)));
        craftingListener.registerCraftingRecipe(new CustomShapedRecipe(CustomMaterial.COFFEE_MACHINE.getItem(),
                item(Material.COBBLESTONE), CustomMaterial.COFFEE_BEAN.getItem(), item(Material.COBBLESTONE),
                item(Material.COBBLESTONE), item(Material.FURNACE), item(Material.COBBLESTONE),
                item(Material.COBBLESTONE), item(Material.IRON_FENCE), item(Material.COBBLESTONE)));
        craftingListener.registerCraftingRecipe(new CustomShapedRecipe(CustomMaterial.MUG.getItem(),
                item(Material.STONE), CustomMaterial.COFFEE_BEAN.getItem(), item(Material.STONE),
                item(Material.STONE), item(Material.FLOWER_POT_ITEM), item(Material.STONE),
                item(Material.STONE), item(Material.STONE), item(Material.STONE)));
    }

    private ItemStack item(Material material) {
        return new ItemStack(material);
    }
}
