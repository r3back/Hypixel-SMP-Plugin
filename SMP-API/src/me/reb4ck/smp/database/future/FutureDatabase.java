package me.reb4ck.smp.database.future;

import java.util.concurrent.CompletableFuture;

public interface FutureDatabase {
    CompletableFuture<Void> getFuture();

    void complete();
}
