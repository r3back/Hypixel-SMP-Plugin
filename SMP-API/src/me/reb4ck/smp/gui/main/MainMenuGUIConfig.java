package me.reb4ck.smp.gui.main;

import lombok.NoArgsConstructor;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.gui.SimpleGUI;
import me.reb4ck.smp.utils.item.Item;

import java.util.List;

@NoArgsConstructor
public final class MainMenuGUIConfig extends SimpleGUI {
    public Item allServers;
    public Item joinRandomServer;
    public Item featured;
    public Item featuredEmpty;
    public List<Integer> featuredSlots;
    public Item fillFavoriteServer;
    public Item emptyFavoriteServer;
    public Item lockedFavoriteServer;
    public List<Integer> favoriteSlots;
    public Item notFoundServer;
    public Item createServer;

    public MainMenuGUIConfig(String title, int size, Background background, Item closeGUI, Item allServers, Item joinRandomServer, Item featured, Item featuredEmpty, List<Integer> featuredSlots,
                             Item notFoundServer, Item fillFavoriteServer, Item emptyFavoriteServer, Item lockedFavoriteServer, Item createServer, List<Integer> favoriteSlots) {
        super(title, size, background, closeGUI);
        this.allServers = allServers;
        this.featuredEmpty = featuredEmpty;
        this.joinRandomServer = joinRandomServer;
        this.featured = featured;
        this.notFoundServer = notFoundServer;
        this.featuredSlots = featuredSlots;
        this.fillFavoriteServer = fillFavoriteServer;
        this.emptyFavoriteServer = emptyFavoriteServer;
        this.lockedFavoriteServer = lockedFavoriteServer;
        this.createServer = createServer;
        this.favoriteSlots = favoriteSlots;
    }
}
