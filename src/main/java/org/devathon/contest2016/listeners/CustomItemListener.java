package org.devathon.contest2016.listeners;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.devathon.contest2016.DevathonPlugin;
import org.devathon.contest2016.blocks.CustomBlock;
import org.devathon.contest2016.recipe.CustomMaterial;

/**
 *
 * @author Autom
 */
public class CustomItemListener implements Listener {

    private static final int foodPoints = 3;
    private static final int saturationPoints = 3;
    private static final int decrease = 10;
    private final DevathonPlugin plugin;

    public CustomItemListener(DevathonPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if ((event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) || event.getHand() != EquipmentSlot.HAND
                || !CustomMaterial.COFFEE.equals(event.getItem())) {
            return;
        }

        final String coffeeName = CustomMaterial.COFFEE.getName();
        ItemMeta itemMeta = event.getItem().getItemMeta();
        int remaining = 100;
        if (!itemMeta.getDisplayName().equals(coffeeName)) {
            remaining = Integer.parseInt(itemMeta.getDisplayName().substring(coffeeName.length() + 2, itemMeta.getDisplayName().length() - 2));
        }

        remaining -= decrease;

        event.getPlayer().setFoodLevel(event.getPlayer().getFoodLevel() + (int) (foodPoints * remaining >= 0 ? 1 : (-1.0 / remaining)));
        event.getPlayer().setSaturation(event.getPlayer().getSaturation() + (int) (saturationPoints * remaining >= 0 ? 1 : (-1.0 / remaining)));

        if (remaining > 0) {
            itemMeta.setDisplayName(coffeeName + " (" + remaining + "%)");
            event.getItem().setItemMeta(itemMeta);
        } else {
            event.getPlayer().getInventory().remove(event.getItem());
        }
        event.getPlayer().updateInventory();
        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_DRINK, 1, 1);
    }

}
