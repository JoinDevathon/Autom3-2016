package org.devathon.contest2016.listeners;

import gnu.trove.map.hash.THashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.devathon.contest2016.DevathonPlugin;
import org.devathon.contest2016.recipe.CustomShapedRecipe;
import org.devathon.contest2016.recipe.CustomShapelessRecipe;

/**
 *
 * @author Autom
 */
public class CraftingListener implements Listener {

    private final List<CustomShapelessRecipe> shapelessRecipes = new ArrayList<>();
    private final List<CustomShapedRecipe> shapedRecipes = new ArrayList<>();

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        boolean customCraft = false;

        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item == null || item.getItemMeta() == null) {
                continue;
            }
            final List<String> lore = item.getItemMeta().getLore();
            if (lore != null && !lore.isEmpty() && lore.get(0).equals(DevathonPlugin.NAME)) {
                customCraft = true;
                break;
            }
        }

        if (!customCraft) {
            return;
        }

        outer:
        for (CustomShapelessRecipe shapelessRecipe : shapelessRecipes) {
            final Map<ItemStack, Integer> amounts = shapelessRecipe.getIngredientAmounts();

            for (ItemStack item : event.getInventory().getMatrix()) {
                Integer amount = amounts.get(item);
                if (amount == null) {
                    continue;
                }
                amount -= item.getAmount();
                amounts.put(item, amount);
            }

            for (Integer value : amounts.values()) {
                if (value > 0) {
                    continue outer;
                }
            }
            return;
        }

        outer:
        for (CustomShapedRecipe shapedRecipe : shapedRecipes) {
            for (int i = 0; i < ((CraftingInventory) event.getInventory()).getMatrix().length; i++) {
                if (shapedRecipe.getRecipe()[i] == null) {
                    continue;
                }
                final ItemStack item = ((CraftingInventory) event.getInventory()).getMatrix()[i];
                if (item == null || !item.equals(shapedRecipe.getRecipe()[i])) {
                    continue outer;
                }
            }
            return;
        }

        event.getInventory().setResult(new ItemStack(Material.AIR));
    }

    public void registerCraftingRecipe(Recipe recipe) {
        if (recipe instanceof CustomShapelessRecipe) {
            final CustomShapelessRecipe customShapelessRecipe = (CustomShapelessRecipe) recipe;
            final ShapelessRecipe shapelessRecipe = new ShapelessRecipe(recipe.getResult());

            for (ItemStack ingredient : customShapelessRecipe.getIngredients()) {
                shapelessRecipe.addIngredient(ingredient.getType(), ingredient.getDurability());
            }

            shapelessRecipes.add(customShapelessRecipe);
            Bukkit.getServer().addRecipe(shapelessRecipe);
        } else if (recipe instanceof CustomShapedRecipe) {
            final CustomShapedRecipe customShapedRecipe = (CustomShapedRecipe) recipe;
            final ShapedRecipe shapedRecipe = new ShapedRecipe(recipe.getResult());

            int i = 0;
            char character = 'a';
            String[] shape = new String[3];
            Map<ItemStack, Character> items = new THashMap<>();
            for (ItemStack ingredient : customShapedRecipe.getRecipe()) {
                final ItemStack itemStack = new ItemStack(ingredient.getType(), 1, ingredient.getDurability());
                Character c = items.get(itemStack);
                if (c == null) {
                    c = character;
                    character++;
                    items.put(itemStack, c);
                }
                if (i % 3 == 0) {
                    shape[i / 3] = "";
                }
                shape[i / 3] += c;
                i++;
            }
            shapedRecipe.shape(shape);
            for (Map.Entry<ItemStack, Character> entry : items.entrySet()) {
                shapedRecipe.setIngredient(entry.getValue(), entry.getKey().getType(), entry.getKey().getDurability());
            }

            shapedRecipes.add(customShapedRecipe);
            Bukkit.getServer().addRecipe(shapedRecipe);
        } else {
            Bukkit.getServer().addRecipe(recipe);
        }
    }

}
