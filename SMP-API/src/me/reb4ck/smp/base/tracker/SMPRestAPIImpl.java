package me.reb4ck.smp.base.tracker;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.service.TrackerService;
import me.reb4ck.smp.api.tracker.SMPRestAPI;
import me.reb4ck.smp.api.tracker.ITrackID;

@Singleton
public final class SMPRestAPIImpl implements SMPRestAPI {
    private final TrackerService trackerService;

    @Inject
    public SMPRestAPIImpl(TrackerService trackerService) {
        this.trackerService = trackerService;
    }

    @Override
    public ITrackID getTrackId(String name) {
        return trackerService.getPremiumUUID(name);
    }
}
