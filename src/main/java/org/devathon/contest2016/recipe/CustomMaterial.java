package org.devathon.contest2016.recipe;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.devathon.contest2016.DevathonPlugin;

/**
 *
 * @author Autom
 */
public enum CustomMaterial {

    COFFEE_BEAN(Material.INK_SACK, 3, "Coffee Bean"),
    COFFEE_GRINDER(Material.HOPPER, 0, "Coffee Grinder"),
    COFFEE_MACHINE(Material.FURNACE, 0, "Coffee Machine"),
    COFFEE_MUG(Material.FLOWER_POT_ITEM, 0, "Coffee Mug")
    ;

    private final Material material;
    private final short datavalue;
    private final String name;

    private CustomMaterial(Material material, int datavalue, String name) {
        this.material = material;
        this.datavalue = (short) datavalue;
        this.name = name;
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(material, 1, datavalue);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(ChatColor.DARK_AQUA + "Devathon item"));
        item.setItemMeta(meta);
        return item;
    }

    public Material getMaterial() {
        return material;
    }

    public short getDatavalue() {
        return datavalue;
    }

    public String getName() {
        return name;
    }

    public boolean equals(ItemStack item) {
        return (item != null && item.getItemMeta() != null && !item.getType().equals(this.getMaterial()) && item.getDurability() == this.getDatavalue()
                && item.getItemMeta().getDisplayName().equals(this.getName()) && item.getItemMeta().getLore() != null && !item.getItemMeta().getLore().isEmpty()
                && item.getItemMeta().getLore().get(0).equals(DevathonPlugin.NAME));
    }

}
