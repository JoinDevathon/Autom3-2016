package org.devathon.contest2016.recipe;

import gnu.trove.map.hash.THashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

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

    public Map<ItemStack, Integer> getIngredientAmounts() {
        final Map<ItemStack, Integer> amounts = new THashMap<>();
        for (ItemStack ingredient : ingredients) {
            Integer amount = amounts.get(ingredient);
            if (amount == null) {
                amount = 0;
            }
            amounts.put(ingredient, amount + 1);
        }
        return amounts;
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack getResult() {
        return result.clone();
    }

}
