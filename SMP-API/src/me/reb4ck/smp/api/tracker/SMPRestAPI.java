package me.reb4ck.smp.api.tracker;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.base.tracker.SMPRestAPIImpl;

@FunctionalInterface
@ImplementedBy(SMPRestAPIImpl.class)
public interface SMPRestAPI {
    ITrackID getTrackId(String name);
}
