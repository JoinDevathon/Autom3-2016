package org.devathon.contest2016;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class DevathonPlugin extends JavaPlugin {

    private final CraftingListener craftingListener = new CraftingListener();

    @Override
    public void onEnable() {
        getLogger().info("Woo! Machines activate!");
        getServer().getPluginManager().registerEvents(craftingListener, this);
        registerCraftingRecipes();
    }

    @Override
    public void onDisable() {
        getLogger().info("Beep boop. Shutting down...");
    }

    private void registerCraftingRecipes() {
        craftingListener.registerCraftingRecipe(new ShapelessRecipe(CustomMaterial.COFFEE_BEAN.getItem()).addIngredient(Material.INK_SACK, 3).addIngredient(Material.SUGAR));
    }

    private ItemStack item(Material material) {
        return new ItemStack(material);
    }
}
