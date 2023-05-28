package me.reb4ck.smp.base.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.reb4ck.smp.api.user.SMPUser;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class SMPFavorites implements SMPUser {
    private String uuid;
    private List<String> favorites;
    private boolean unlimitedServer;

    @JsonIgnore
    public void remove(String server){
        favorites.remove(server);
    }
}
