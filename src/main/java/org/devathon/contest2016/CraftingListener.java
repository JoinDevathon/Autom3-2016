package org.devathon.contest2016;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.devathon.contest2016.recipe.CustomShapedRecipe;
import org.devathon.contest2016.recipe.CustomShapelessRecipe;

/**
 *
 * @author Autom
 */
public class CraftingListener implements Listener {

    private final List<Recipe> shapelessRecipes = new ArrayList<>();
    private final List<Recipe> shapedRecipes = new ArrayList<>();

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        boolean customCraft = false;

        for (ItemStack item : event.getInventory()) {
            final List<String> lore = item.getItemMeta().getLore();
            if (lore != null && !lore.isEmpty() && lore.get(0).equals(ChatColor.DARK_AQUA + "Devathon item")) {
                customCraft = true;
                break;
            }
        }

        if (!customCraft) {
            return;
        }
    }

    public void registerCraftingRecipe(Recipe recipe) {
        if (recipe instanceof CustomShapelessRecipe) {
            shapelessRecipes.add(recipe);
        } else if (recipe instanceof CustomShapedRecipe) {
            shapedRecipes.add(recipe);
        } else {
            Bukkit.getServer().addRecipe(recipe);
        }
    }

}
