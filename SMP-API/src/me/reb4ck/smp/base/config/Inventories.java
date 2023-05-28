package me.reb4ck.smp.base.config;

import com.cryptomorin.xseries.XMaterial;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.ImmutableMap;
import lombok.NoArgsConstructor;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.gui.admin.IndividualServerGUIConfig;
import me.reb4ck.smp.gui.admin_all.AdminAllServersGUIConfig;
import me.reb4ck.smp.gui.allservers.AllServersGUIConfig;
import me.reb4ck.smp.gui.anvil.AddLineDescriptionGUIConfig;
import me.reb4ck.smp.gui.anvil.AnvilGUIConfig;
import me.reb4ck.smp.gui.confirm.ConfirmGUIConfig;
import me.reb4ck.smp.gui.description.DescriptionGUIConfig;
import me.reb4ck.smp.gui.feature.FeatureServerGUIConfig;
import me.reb4ck.smp.gui.feature.SelectServerToFeatureGUIConfig;
import me.reb4ck.smp.gui.main.MainMenuGUIConfig;
import me.reb4ck.smp.gui.ram.RamItem;
import me.reb4ck.smp.gui.ram.RamUpgrade;
import me.reb4ck.smp.gui.ram.RamUpgradeGUIConfig;
import me.reb4ck.smp.utils.builder.ItemBuilder;
import me.reb4ck.smp.utils.item.Item;

import java.util.Arrays;
import java.util.Collections;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Inventories {
    @JsonIgnore
    private final Background background1 = new Background(ImmutableMap.<Integer, Item>builder().build());
    @JsonIgnore
    private final Background background2 = new Background(ImmutableMap.<Integer, Item>builder()
            .put(45, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(46, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(47, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(48, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(50, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(51, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(52, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(53, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .build());
    @JsonIgnore
    private final Background background5 = new Background(ImmutableMap.<Integer, Item>builder()
            .put(0, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(1, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(2, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(3, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(4, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(5, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(6, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(7, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(8, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(9, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(18, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(27, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(28, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(29, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(30, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

            .put(32, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(33, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(34, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(35, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(17, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(26, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .build());

    @JsonIgnore
    private final Background background3 = new Background(ImmutableMap.<Integer, Item>builder()
            .put(0, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(1, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(2, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(3, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(4, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(5, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(6, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(7, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(8, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(9, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(18, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(27, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(36, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(45, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(17, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(26, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(35, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(44, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(53, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(46, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(47, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(50, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(51, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .put(52, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .build());


    public AllServersGUIConfig allServersGUIConfig = new AllServersGUIConfig("Servers list (server.com)", 54, background1,
            ItemBuilder.of(XMaterial.BARRIER, 49, 1, "&cClose Menu", Collections.emptyList())
                    .enabled(false)
                    .build(),
            ItemBuilder.of(XMaterial.BOOK, 46, 1, "&7Back Page", Collections.emptyList())
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.BOOK, 52, 1, "&7Next Page", Collections.emptyList())
                    .enabled(true)
                    .build(),

            ItemBuilder.of(XMaterial.ARROW, 49, 1, "&dGo back", Collections.emptyList())
                    .enabled(true)
                    .build(),

            ItemBuilder.of(XMaterial.DIAMOND_BLOCK, 1, "&dServer: %server_display_name%", Arrays.asList("",
                    "%server_description%", "", "&e%server_players%&e/&6%server_max_players%", "", "&6» &eLeft-Click to teleport", "&6» &eRight-Click to add to favorite list"))
                    .enabled(true)
                    .build()
            );

    public MainMenuGUIConfig mainMenuGUIConfig = new MainMenuGUIConfig("Where To?", 54, background1,
            ItemBuilder.of(XMaterial.BARRIER, 49, 1, "&cClose Menu", Collections.emptyList())
                    .enabled(false)
                    .build(),
            ItemBuilder.of(XMaterial.BOOK, 10, 1, "&eAll Servers", Arrays.asList("", "&6» &eSee all online servers!"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.GOLD_BLOCK, 19, 1, "&eJoin a Random Server", Arrays.asList("", "&6» &eGo to a random server!"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.STONE, 1, "&eServer: %server_display_name%", Arrays.asList("", "%server_description%", "", "&e%server_players%&e/&6%server_max_players%", "", "", "&6» &eClick to teleport"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.BEDROCK, 1, "&e&lFeatured Servers", Arrays.asList("", "&6» &eClick to view Feature Options"))
                    .enabled(true)
                    .build(),
            Arrays.asList(13,14,15),
            ItemBuilder.of(XMaterial.BEDROCK, 1, "&cNot Found Server", Arrays.asList("", "&eClick to remove this server from your favorite list"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.STONE, 1, "&9%server_name%", Arrays.asList("", "&e%server_players%&e/&6%server_max_players% Playing", "", "&6» &eRight Click to add to/remove from",
                    "&eyour favorites list"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, "&8Empty Favorite", Collections.emptyList())
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, "&cLocked Favorite Slot", Arrays.asList("", "&7Upgrade your rank at &bserver.com", "&7to unlock more slots!"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.ANVIL, 37, 1, "&aCreate your server!", Arrays.asList("", "&6» &eClick to create a new server!"))
                    .enabled(true)
                    .build(),
            Arrays.asList(30,31,32,33,34,39,40,41,42,43)
    );

    public RamUpgradeGUIConfig ramUpgradeGUIConfig = new RamUpgradeGUIConfig("Ram Upgrade", 54, background3,
            ItemBuilder.of(XMaterial.BARRIER, 49, 1, "&cClose Menu", Collections.emptyList())
                    .enabled(true)
                    .build(),

            Arrays.asList(
                    RamItem.builder()
                            .ramUpgrade(new RamUpgrade(3, 2500))
                            .slot(10)
                            .amount(1)
                            .title("&a&lRam Upgrade")
                            .lore(Arrays.asList("", "&2» &7Upgrade to: &a3GB", "&2» &7Price: &a%price% Tokens", "", "%can_upgrade%"))
                            .material(XMaterial.BRICK)
                            .build(),
                    RamItem.builder()
                            .ramUpgrade(new RamUpgrade(4, 3500))
                            .slot(12)
                            .amount(1)
                            .title("&a&lRam Upgrade")
                            .lore(Arrays.asList("", "&2» &7Upgrade to: &a4GB", "&2» &7Price: &a%price% Tokens", "", "%can_upgrade%"))
                            .material(XMaterial.IRON_INGOT)
                            .build(),
                    RamItem.builder()
                            .ramUpgrade(new RamUpgrade(5, 4500))
                            .slot(14)
                            .amount(1)
                            .title("&a&lRam Upgrade")
                            .lore(Arrays.asList("", "&2» &7Upgrade to: &a5GB", "&2» &7Price: &a%price% Tokens", "", "%can_upgrade%"))
                            .material(XMaterial.GOLD_INGOT)
                            .build(),
                    RamItem.builder()
                            .ramUpgrade(new RamUpgrade(6, 5500))
                            .slot(16)
                            .amount(1)
                            .title("&a&lRam Upgrade")
                            .lore(Arrays.asList("", "&2» &7Upgrade to: &a6GB", "&2» &7Price: &a%price% Tokens", "", "%can_upgrade%"))
                            .material(XMaterial.DIAMOND)
                            .build()
                    ),

            ItemBuilder.of(XMaterial.FURNACE, 31, 1, "&aServer Ram", Arrays.asList("", "&2» &7Your server ram: &a%server_ram%GB"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.ARROW, 48, 1, "&eGo Back", Arrays.asList("", "&6» &eClick to go back"))
                    .enabled(true)
                    .build()

            );


    public FeatureServerGUIConfig featureServerGUIConfig = new FeatureServerGUIConfig("Confirm Feature Server", 54, background3,
            ItemBuilder.of(XMaterial.BARRIER, 49, 1, "&cClose Menu", Collections.emptyList())
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.GOLD_NUGGET, 22, 1, "&aConfirm", Arrays.asList(
                    "",
                    "&7Feature Time: &b%feature_duration%",
                    "&7Feature Price: %feature_price%",
                    "",
                    "&6» &eClick to confirm server feature!"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.ARROW, 48, 1, "&eGo back", Arrays.asList("", "&6» &eClick to go back!"))
                    .enabled(true)
                    .build()
    );

    public AnvilGUIConfig anvilGUIConfig = AnvilGUIConfig.builder()
            .leftItem(ItemBuilder.of(XMaterial.IRON_SWORD, 1, "", Collections.emptyList()).build())
            .rightItem(ItemBuilder.of(XMaterial.IRON_SWORD, 1, "", Collections.emptyList()).build())
            .title("Server Creator")
            .startTitle("Type a name...")
            .build();

    public AddLineDescriptionGUIConfig addLineDescriptionGUIConfig = AddLineDescriptionGUIConfig.builder()
            .leftItem(ItemBuilder.of(XMaterial.IRON_SWORD, 1, "", Collections.emptyList()).build())
            .rightItem(ItemBuilder.of(XMaterial.IRON_SWORD, 1, "", Collections.emptyList()).build())
            .title("Description editor")
            .startTitle("Type a line...")
            .build();

    public IndividualServerGUIConfig individualServerGUIConfig = new IndividualServerGUIConfig("Admin GUI", 54, background3,
            ItemBuilder.of(XMaterial.BARRIER, 49, 1, "&cClose Menu", Collections.emptyList())
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.RED_WOOL, 19, 1, "&eDelete Server", Arrays.asList("", "&6» &eClick to delete your server!"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 20, 1, "&eStop Server", Arrays.asList("", "&7Status: &6%server_status%", "", "&6» &eClick to stop your server!"))
                    .enabled(true)
                    .build(),

            ItemBuilder.of(XMaterial.LIME_STAINED_GLASS_PANE, 21, 1,"&eStart Server", Arrays.asList("", "&7Status: &6%server_status%", "", "&6» &eClick to start your server!"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.COMPASS, 22, 1, "&eTeleport to your server", Arrays.asList("", "&6» &eClick to teleport to your server"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.PAPER, 23, 1, "&eEdit description", Arrays.asList("", "&6» &eClick to edit server description"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.MAP, 24, 1, "&6Server Info", Arrays.asList("",
                    "&7Server Name: &e%server_name%",
                    "",
                    "&7Server Display Name: &6%server_display_name%",
                    "",
                    "&7Players: &e%server_players%&e/&6%server_max_players%",
                    "",
                    "&7Featured: %time_format%",
                    "",
                    "&7Status: &6%server_status%",
                    ""))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.ARROW, 48, 1, "&eGo Back", Arrays.asList("", "&6» &eClick to go back"))
                    .enabled(true)
                    .build(),

            ItemBuilder.of(XMaterial.FURNACE, 25, 1, "&6Ram Upgrade", Arrays.asList("",
                    "&2» &7Current Ram: &a%server_ram%",
                    "",
                    "&e Click to view Upgrades!"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.BOOK, 30, 1, "&6SMP Help", Arrays.asList("",
                    "&2» &7Type &a/SMP Help &7- to view",
                    "&7all available commands"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.PLAYER_HEAD, 32, 1, "&6Get Op", Arrays.asList("",
                    "&2» &7Click to get op in this smp server!"))
                    .enabled(true)
                    .headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDE3NDM0OWY3OTMxMWQxMDRkNzkxN2QzMmJmN2EwZGNlZTQyMzQyMWNhOWU4YTEzMWYyZDQwMmEzYzUzODU3MiJ9fX0=")
                    .build()

    );

    public ConfirmGUIConfig confirmDeleteGUI = new ConfirmGUIConfig("Confirm", 27, background1,
            ItemBuilder.of(XMaterial.BARRIER, 49, 1, "&cClose Menu", Collections.emptyList())
                    .enabled(false)
                    .build(),
            ItemBuilder.of(XMaterial.LIME_WOOL, 11, 1, "&eConfirm", Arrays.asList("", "&6» &eClick to confirm"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.RED_WOOL, 15, 1, "&eCancel", Arrays.asList("", "&6» &eClick to cancel"))
                    .enabled(true)
                    .build()
    );

    public AdminAllServersGUIConfig adminAllServersGUIConfig = new AdminAllServersGUIConfig("Your Servers", 36, background5,
            ItemBuilder.of(XMaterial.BARRIER, 31, 1, "&cClose Menu", Collections.emptyList())
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.BOOK, 1, "&7Server: &6%server_name%", Arrays.asList(
                    "",
                    "&7Players: &e%server_players%&e/&6%server_max_players%",
                    "",
                    "&7Click to manage"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.BEDROCK, 1, "&eLocked Slot:", Arrays.asList("", "&7Buy Rank to Unlock this slot!"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.PAPER, 1, "&eEmpty Slot", Arrays.asList("", "&6» &eCreate a server using /smp create <name>"))
                    .enabled(true)
                    .build(),
            Arrays.asList(10,11,12,13,14,15,16)
    );

    public DescriptionGUIConfig descriptionGUIConfig = new DescriptionGUIConfig("Edit Description", 36, background5,
            ItemBuilder.of(XMaterial.BARRIER, 31, 1, "&eClose menu", Collections.emptyList())
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.HOPPER, 10, 1, "&7Reset Description", Arrays.asList(
                    "",
                    "&6» &7Click to reset description"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.BARRIER, 12, 1, "&7Remove last line", Arrays.asList(
                    "",
                    "&6» &7Click to remove last description line"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.LIME_DYE,  14,1, "&eAdd a new line", Arrays.asList("", "&6» &eClick to add a new description line"))
                    .enabled(true)
                    .build(),

            ItemBuilder.of(XMaterial.MAP,  16,1, "&eCurrent Description:", Collections.singletonList("%server_description%"))
                        .enabled(true)
                        .build(),

        ItemBuilder.of(XMaterial.ARROW,  30,1, "&eGo back", Arrays.asList("", "&6» &eClick to go back"))
                .enabled(true)
                .build()
    );

    public SelectServerToFeatureGUIConfig selectServerToFeature = new SelectServerToFeatureGUIConfig("Select a Server to Feature", 36, background5,
            ItemBuilder.of(XMaterial.BARRIER, 31, 1, "&cClose Menu", Collections.emptyList())
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.BOOK, 1, "&7Server: &6%server_name%", Arrays.asList(
                    "",
                    "&7Players: &e%server_players%&e/&6%server_max_players%",
                    "",
                    "&6» &eClick to manage"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.BEDROCK, 1, "&eLocked Slot:", Arrays.asList("", "&7Buy Rank to Unlock this slot!"))
                    .enabled(true)
                    .build(),
            ItemBuilder.of(XMaterial.PAPER, 1, "&eEmpty Slot", Arrays.asList("", "&6» &eCreate a server using /smp create <name>"))
                    .enabled(true)
                    .build(),
            Arrays.asList(10,11,12,13,14,15,16),

            ItemBuilder.of(XMaterial.ARROW, 30, 1, "&eGo back", Collections.singletonList("&6» &eClick to go back"))
                    .enabled(true)
                    .build()
    );
}
