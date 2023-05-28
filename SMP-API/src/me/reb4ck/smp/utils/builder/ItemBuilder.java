package me.reb4ck.smp.utils.builder;

import com.cryptomorin.xseries.XMaterial;
import me.reb4ck.smp.utils.item.Item;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ItemBuilder {
    private final Item item = new Item();

    public static ItemBuilder of(){
        return new ItemBuilder();
    }

    public static ItemBuilder of(XMaterial material, int amount, String title, List<String> lore){
        ItemBuilder itemBuilder = new ItemBuilder();
        itemBuilder
                .material(material)
                .amount(amount)
                .title(title)
                .lore(lore);
        return itemBuilder;
    }

    public static ItemBuilder of(XMaterial material, int slot, int amount, String title, List<String> lore){
        ItemBuilder itemBuilder = of(material, amount, title, lore);
        itemBuilder.slot(slot);
        return itemBuilder;
    }

    public ItemBuilder material(XMaterial material){
        item.material = material;
        return this;
    }

    public ItemBuilder slot(int slot){
        item.slot = slot;
        return this;
    }

    public ItemBuilder amount(int amount){
        item.amount = amount;
        return this;
    }

    public ItemBuilder lore(List<String> lore){
        item.lore = lore;
        return this;
    }

    public ItemBuilder lore(String... lore){
        item.lore = Arrays.stream(lore).collect(Collectors.toList());
        return this;
    }

    public ItemBuilder title(String title){
        item.title = title;
        return this;
    }

    public ItemBuilder command(String command){
        item.command = command;
        return this;
    }

    public ItemBuilder enabled(boolean enabled){
        item.enabled = enabled;
        return this;
    }

    public ItemBuilder headData(String headData){
        item.headData = headData;
        return this;
    }

    public ItemBuilder headOwner(String headOwner){
        item.headOwner = headOwner;
        return this;
    }

    public Item build(){
        return this.item;
    }
}
