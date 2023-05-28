package me.reb4ck.smp.api.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.base.user.SMPOwner;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class FavoriteList {
    private List<SMPFavorites> smpFavoritesList;
}
