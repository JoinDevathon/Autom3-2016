package org.devathon.contest2016.recipe;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.devathon.contest2016.CustomMaterial;

/**
 *
 * @author Autom
 */
public class CustomShapelessRecipe implements Recipe {

    private final ItemStack result;
    private final List<ItemStack> ingredients = new ArrayList<>();

    public CustomShapelessRecipe(ItemStack result) {
        this.result = result;
    }

    public CustomShapelessRecipe addIngredient(Material material) {
        ingredients.add(new ItemStack(material));
        return this;
    }

    public CustomShapelessRecipe addIngredient(Material material, int datavalue) {
        ingredients.add(new ItemStack(material, 1, (short) datavalue));
        return this;
    }

    public CustomShapelessRecipe addIngredient(CustomMaterial material) {
        ingredients.add(material.getItem());
        return this;
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

}
