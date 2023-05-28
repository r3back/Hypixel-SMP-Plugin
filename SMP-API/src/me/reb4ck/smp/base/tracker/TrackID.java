package me.reb4ck.smp.base.tracker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.reb4ck.smp.api.tracker.ITrackID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class TrackID implements ITrackID {
    private String value;
}
