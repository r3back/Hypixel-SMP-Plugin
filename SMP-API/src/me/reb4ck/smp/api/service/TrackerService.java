package me.reb4ck.smp.api.service;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.base.service.TrackerServiceImpl;

@FunctionalInterface
@ImplementedBy(TrackerServiceImpl.class)
public interface TrackerService {
    ITrackID getPremiumUUID(String name);
}
