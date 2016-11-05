package org.devathon.contest2016.recipe;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

/**
 *
 * @author Autom
 */
public class CustomShapedRecipe implements Recipe {
    
    private final ItemStack result;
    private final ItemStack[] recipe;

    public CustomShapedRecipe(ItemStack result, ItemStack... items) {
        this.result = result;
        recipe = items;
    }

    public ItemStack[] getRecipe() {
        return recipe;
    }

    @Override
    public ItemStack getResult() {
        return result.clone();
    }

}
