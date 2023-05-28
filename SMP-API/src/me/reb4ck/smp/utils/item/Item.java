package me.reb4ck.smp.utils.item;

import com.cryptomorin.xseries.XMaterial;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@SuperBuilder
public class Item {
    public XMaterial material;
    public Integer amount;
    public String title;
    public String headData;
    public String headOwner;
    public List<String> lore;
    public Integer slot;
    public String command;
    public boolean enabled;

    public Item(XMaterial material, int amount, String displayName, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.title = displayName;
    }

    public Item(XMaterial material, int slot, int amount, String displayName, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.title = displayName;
        this.slot = slot;
    }

    public Item(XMaterial material, int slot, String headData, int amount, String displayName, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.title = displayName;
        this.slot = slot;
        this.headData = headData;
    }

    public Item(XMaterial material, int slot, int amount, String displayName, String headOwner, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.title = displayName;
        this.headOwner = headOwner;
        this.slot = slot;
    }

    public Item(XMaterial material, int amount, String displayName, String headOwner, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.title = displayName;
        this.headOwner = headOwner;
    }

    public Item() {
    }
}