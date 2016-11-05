package org.devathon.contest2016;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.recipe.CustomShapedRecipe;
import org.devathon.contest2016.recipe.CustomShapelessRecipe;

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
        craftingListener.registerCraftingRecipe(new CustomShapedRecipe(CustomMaterial.COFFEE_GRINDER.getItem(),
                item(Material.COBBLESTONE), item(Material.HOPPER), item(Material.COBBLESTONE),
                item(Material.COBBLESTONE), CustomMaterial.COFFEE_BEAN.getItem(), item(Material.COBBLESTONE),
                item(Material.COBBLESTONE), item(Material.CAULDRON_ITEM), item(Material.COBBLESTONE)));
        final ItemStack bean = CustomMaterial.COFFEE_BEAN.getItem();
        bean.setAmount(2);
        craftingListener.registerCraftingRecipe(new CustomShapelessRecipe(bean).addIngredient(CustomMaterial.COFFEE_BEAN).addIngredient(Material.INK_SACK, 3));
    }

    private ItemStack item(Material material) {
        return new ItemStack(material);
    }
}
