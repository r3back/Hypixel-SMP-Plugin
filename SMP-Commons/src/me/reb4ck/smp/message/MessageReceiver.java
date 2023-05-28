package me.reb4ck.smp.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MessageReceiver {
    TOKEN_MANAGER_RECEIVER("TM_RECEIVER"),
    RECEIVER("SOCKET_RECEIVER_SMP");

    @Getter
    private final String name;
}
