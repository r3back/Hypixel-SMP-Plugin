package me.reb4ck.smp.api.service;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.base.service.SMPFavoritesService;
import me.reb4ck.smp.base.user.SMPFavorites;

import java.util.List;
import java.util.Optional;

@ImplementedBy(SMPFavoritesService.class)
public interface FavoriteService {
    Optional<SMPFavorites> getFavorites(String playerName, ToSearch toSearch);
    void addFavorite(SMPFavorites smpOwner);
    void removeUser(SMPFavorites smpUser);
    List<SMPFavorites> getFavorites();

    public enum ToSearch{
        UUID,
        NAME
    }
}
