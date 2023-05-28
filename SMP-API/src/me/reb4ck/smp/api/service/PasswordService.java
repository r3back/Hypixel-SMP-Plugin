package me.reb4ck.smp.api.service;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.base.service.PassWordServiceImpl;

@FunctionalInterface
@ImplementedBy(PassWordServiceImpl.class)
public interface PasswordService {
    String generatePassayPassword();
}
