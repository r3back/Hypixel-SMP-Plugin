package me.reb4ck.smp.utils;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.utils.item.Item;
import me.reb4ck.smp.api.placeholder.IPlaceholder;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class InventoryUtils {

    public static ItemStack makeItem(XMaterial material, int amount, String name, List<String> lore) {
        ItemStack item = material.parseItem();
        if (item == null)
            return null;
        item.setAmount(amount);
        ItemMeta m = item.getItemMeta();
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if(lore != null && lore.size() > 0)
            m.setLore(StringUtils.color(lore));
        m.setDisplayName(StringUtils.color(name == null ? " " : name));
        item.setItemMeta(m);
        return item;
    }

    public static boolean isNull(ItemStack itemStack){
        return (itemStack == null || itemStack.getType() == Material.AIR);
    }


    public static ItemStack makeItem(Item item) {
        try {
            ItemStack itemstack = makeItem(item.material, item.amount, item.title, item.lore);
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                return makeItem(itemstack, item);
            } else if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            e.printStackTrace();
            return makeItem(XMaterial.STONE, item.amount, item.title, item.lore);
        }
    }

    public static ItemStack makeItem(Item item, List<IPlaceholder> placeholders) {
        try {
            ItemStack itemstack = makeItem(item.material, item.amount, StringUtils.processMultiplePlaceholders(item.title, placeholders), StringUtils.processMultiplePlaceholders(item.lore, placeholders));
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                return makeItem(itemstack, item);
            }
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(StringUtils.processMultiplePlaceholders(item.headOwner, placeholders));
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            e.printStackTrace();
            return makeItem(XMaterial.STONE, item.amount, StringUtils.processMultiplePlaceholders(item.title, placeholders), StringUtils.processMultiplePlaceholders(item.lore, placeholders));
        }
    }


    public static ItemStack makeItem(Item item, List<IPlaceholder> placeholders, SMPServer server) {
        try {
            XMaterial material;

            String logo = server.getLogo().getMaterial();

            try {
                if(logo == null) throw new Exception();

                material = XMaterial.valueOf(logo);
            }catch (Exception e){
                material = XMaterial.DIAMOND_BLOCK;
            }

            List<String> lore = new ArrayList<String>(){{
                for(String line : item.lore){
                    if(line.contains("%server_description%")){
                        if (server.getDescription() != null) server.getDescription().forEach(toAdd -> add(StringUtils.color(toAdd)));
                        continue;
                    }
                    add(line);
                }
            }};

            ItemStack itemstack = makeItem(material, item.amount, StringUtils.processMultiplePlaceholders(item.title, placeholders), StringUtils.processMultiplePlaceholders(lore, placeholders));

            if(server.getLogo().isEnchanted()){
                ItemMeta meta = itemstack.getItemMeta();
                meta.addEnchant(Enchantment.KNOCKBACK, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemstack.setItemMeta(meta);
            }
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                return makeItem(itemstack, item);
            }
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(StringUtils.processMultiplePlaceholders(item.headOwner, placeholders));
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            e.printStackTrace();
            return makeItem(XMaterial.STONE, item.amount, StringUtils.processMultiplePlaceholders(item.title, placeholders), StringUtils.processMultiplePlaceholders(item.lore, placeholders));
        }
    }

    private static ItemStack makeItem(ItemStack itemStack, Item item){
        NBTItem nbtItem = new NBTItem(itemStack);
        NBTCompound skull = nbtItem.addCompound("SkullOwner");
        skull.setString("Name", "tr7zw");
        skull.setString("Id", UUID.randomUUID().toString());
        NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
        texture.setString("Value", item.headData);
        return nbtItem.getItem();
    }

    public static void fillInventory(Inventory inventory, Background background) {
        if (background.items == null) return;
        for (int slot : background.items.keySet()) {
            if (slot >= inventory.getSize()) continue;
            inventory.setItem(slot, makeItem(background.items.get(slot)));
        }
    }
}
