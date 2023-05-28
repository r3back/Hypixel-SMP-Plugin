package me.reb4ck.smp.api.status;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.base.status.SMPStatusImpl;

@ImplementedBy(SMPStatusImpl.class)
public interface SMPStatus {
    void powerOn();
    void powerOff();
}
