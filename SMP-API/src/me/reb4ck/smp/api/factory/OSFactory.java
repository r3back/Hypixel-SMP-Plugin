package me.reb4ck.smp.api.factory;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.api.service.OSService;
import me.reb4ck.smp.factory.OSFactoryImpl;

@ImplementedBy(OSFactoryImpl.class)
public interface OSFactory {
    OSService getOSService();
}
