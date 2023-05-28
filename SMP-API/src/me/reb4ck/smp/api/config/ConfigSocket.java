package me.reb4ck.smp.api.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public final class ConfigSocket {
    public String ip;
    public int port;
    public String name;
    public String password;
    public boolean enabled;
}
