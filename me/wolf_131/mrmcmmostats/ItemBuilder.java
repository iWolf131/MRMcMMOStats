package me.wolf_131.mrmcmmostats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemBuilder {

    private ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1, (short) 0);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is.clone();
    }

    public ItemBuilder(Material m, int amount, short data) {
        is = new ItemStack(m, amount, data);
    }

    public ItemBuilder durability(int dur) {
        is.setDurability((short) dur);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder owner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemBuilder lore(String... lore) {
        ItemMeta im = is.getItemMeta();
        List<String> out = im.getLore() == null ? new ArrayList<>() : im.getLore();
        for (String string : lore)
            out.add(ChatColor.translateAlternateColorCodes('&', string));
        im.setLore(out);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder listLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        List<String> out = im.getLore() == null ? new ArrayList<>() : im.getLore();
        for (String string : lore)
            out.add(ChatColor.translateAlternateColorCodes('&', string));
        im.setLore(out);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeAttributes() {
        ItemMeta meta = is.getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        is.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return is;
    }
}
