package me.reb4ck.smp.base.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.service.FavoriteService;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.api.tracker.SMPRestAPI;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.utils.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public final class SMPFavoritesService implements FavoriteService {
    private final List<SMPFavorites> smpFavorites = new ArrayList<>();
    private final SMPRestAPI smpRestAPI;

    @Inject
    public SMPFavoritesService(SMPRestAPI smpRestAPI) {
        this.smpRestAPI = smpRestAPI;
    }

    @Override
    public List<SMPFavorites> getFavorites() {
        return smpFavorites;
    }

    @Override
    public Optional<SMPFavorites> getFavorites(String playerName, ToSearch toSearch) {
        if(toSearch.equals(ToSearch.NAME)){
            ITrackID trackID = smpRestAPI.getTrackId(playerName);

            if(trackID == null || trackID.getValue() == null) return Optional.empty();

            return smpFavorites.stream()
                    .filter(smpFavorites1 -> smpFavorites1.getUuid() != null)
                    .filter(smpFavorites -> smpFavorites.getUuid().equals(trackID.getValue())).findFirst();
        }else{
            return smpFavorites.stream().filter(smpFavorites -> smpFavorites.getUuid().equals(playerName)).findFirst();
        }
    }

    @Override
    public void addFavorite(SMPFavorites smpOwner) {
        smpFavorites.add(smpOwner);
    }

    @Override
    public void removeUser(SMPFavorites smpUser) {
        smpFavorites.remove(smpUser);
    }
}
