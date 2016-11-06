package org.devathon.contest2016.recipe;

import java.util.Arrays;
import java.util.UUID;
import net.minecraft.server.v1_10_R1.AttributeModifier;
import net.minecraft.server.v1_10_R1.EnumItemSlot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.devathon.contest2016.DevathonPlugin;

/**
 *
 * @author Autom
 */
public enum CustomMaterial {

    COFFEE_BEAN(Material.INK_SACK, 3, "Coffee Bean", true),
    COFFEE_GRINDER(Material.HOPPER, 0, "Coffee Grinder", true),
    COFFEE_MACHINE(Material.FURNACE, 0, "Coffee Machine", true),
    COFFEE_MUG(Material.FLOWER_POT_ITEM, 0, "Coffee Mug", true),
    GROUND_COFFEE_BEANS(Material.MELON_SEEDS, 0, "Ground Coffee Beans", true),
    COFFEE(Material.FLOWER_POT_ITEM, 0, "Coffee", false)
    ;

    private final Material material;
    private final short datavalue;
    private final String name;
    private final boolean stackable;

    private CustomMaterial(Material material, int datavalue, String name, boolean stackable) {
        this.material = material;
        this.datavalue = (short) datavalue;
        this.name = name;
        this.stackable = stackable;
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(material, 1, datavalue);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(ChatColor.DARK_AQUA + "Devathon item"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        if (!stackable) {
            net.minecraft.server.v1_10_R1.ItemStack copy = CraftItemStack.asNMSCopy(item);
            copy.a("generic.luck", new AttributeModifier(UUID.randomUUID(), "generic.luck", 0, 0), EnumItemSlot.MAINHAND);
            return CraftItemStack.asBukkitCopy(copy);
        }
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
        return (item != null && item.getItemMeta() != null && item.getType() == this.getMaterial() && item.getDurability() == this.getDatavalue()
                && item.getItemMeta().getDisplayName().equals(this.getName()) && item.getItemMeta().getLore() != null && !item.getItemMeta().getLore().isEmpty()
                && item.getItemMeta().getLore().get(0).equals(DevathonPlugin.NAME));
    }

}
