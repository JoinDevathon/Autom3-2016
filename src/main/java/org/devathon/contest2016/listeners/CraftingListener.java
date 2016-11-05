package org.devathon.contest2016.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
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
        for (ItemStack item : event.getInventory()) {
            if (item == null || item.getItemMeta() == null) {
                continue;
            }
            final List<String> lore = item.getItemMeta().getLore();
            if (lore != null && !lore.isEmpty() && lore.get(0).equals(ChatColor.DARK_AQUA + "Devathon item")) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }

    @EventHandler
    public void onCustomCraftPrepare(InventoryClickEvent event) {
        if (event.getSlotType() != SlotType.CRAFTING) {
            return;
        }

        boolean customCraft = false;

        ItemStack current = event.getCurrentItem();
        event.setCurrentItem(event.getCursor());

        for (ItemStack item : event.getClickedInventory()) {
            if (item == null || item.getItemMeta() == null) {
                continue;
            }
            final List<String> lore = item.getItemMeta().getLore();
            if (lore != null && !lore.isEmpty() && lore.get(0).equals(ChatColor.DARK_AQUA + "Devathon item")) {
                customCraft = true;
                break;
            }
        }

        if (!customCraft) {
            event.setCurrentItem(current);
            return;
        }

        outer:
        for (CustomShapelessRecipe shapelessRecipe : shapelessRecipes) {
            final Map<ItemStack, Integer> amounts = shapelessRecipe.getIngredientAmounts();

            for (ItemStack item : event.getClickedInventory()) {
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

            //This is supposed to work, but apparently it doesn't :(
            //((CraftingInventory) event.getClickedInventory()).setResult(shapelessRecipe.getResult());
            //I guess I'll have to use a different solution
            event.setCurrentItem(current);
            craftItem(event.getWhoClicked(), event.getClickedInventory(), shapelessRecipe.getResult());
            return;
        }

        outer:
        for (CustomShapedRecipe shapedRecipe : shapedRecipes) {
            for (int i = 0; i < ((CraftingInventory) event.getClickedInventory()).getMatrix().length; i++) {
                if (shapedRecipe.getRecipe()[i] == null) {
                    continue;
                }
                final ItemStack item = ((CraftingInventory) event.getClickedInventory()).getMatrix()[i];
                if (item == null || !item.equals(shapedRecipe.getRecipe()[i])) {
                    continue outer;
                }
            }

            event.setCurrentItem(current);
            craftItem(event.getWhoClicked(), event.getClickedInventory(), shapedRecipe.getResult());
            return;
        }

        event.setCurrentItem(current);
    }

    private void craftItem(HumanEntity player, Inventory inventory, ItemStack result) {
        inventory.clear();
        player.setItemOnCursor(null);
        player.getWorld().dropItemNaturally(player.getLocation(), result);
        player.closeInventory();
    }

    public void registerCraftingRecipe(Recipe recipe) {
        if (recipe instanceof CustomShapelessRecipe) {
            shapelessRecipes.add((CustomShapelessRecipe) recipe);
        } else if (recipe instanceof CustomShapedRecipe) {
            shapedRecipes.add((CustomShapedRecipe) recipe);
        } else {
            Bukkit.getServer().addRecipe(recipe);
        }
    }

}
