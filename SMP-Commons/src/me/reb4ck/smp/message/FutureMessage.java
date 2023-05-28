package me.reb4ck.smp.message;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public final class FutureMessage {
    private UUID uuid;
    private String reply;
    private String owner;
    private String name;
    private String permission;
    private FutureType futureType;
    private SearchType searchType;

    public enum FutureType{
        SHUTDOWN_REQUEST,

        FEATURE_REQUEST,

        IS_UNLIMITED_REQUEST,

        RAM_PRICES,

        SMP_SERVER,

        FAVORITE_NAMES,

        SERVER_SLOTS,

        FAVORITE_SLOTS,

        MONEY
    }

    public enum SearchType{
        BY_NAME,
        BY_OWNER,
        BY_OWNER_AND_NAME,
        ALL_PLAYER,
        ALL;
    }
}
