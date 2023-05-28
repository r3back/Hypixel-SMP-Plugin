package me.reb4ck.smp.gui.ram;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.reb4ck.smp.utils.item.Item;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public final class RamItem extends Item {
    public RamUpgrade ramUpgrade;
}
