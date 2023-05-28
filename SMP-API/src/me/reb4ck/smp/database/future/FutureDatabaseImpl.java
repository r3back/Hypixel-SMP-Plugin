package me.reb4ck.smp.database.future;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.concurrent.CompletableFuture;

@Singleton
public final class FutureDatabaseImpl implements FutureDatabase {
    private final CompletableFuture<Void> future = new CompletableFuture<>();

    @Inject
    public FutureDatabaseImpl() {
    }

    @Override
    public CompletableFuture<Void> getFuture() {
        return future;
    }

    @Override
    public void complete() {
        future.complete(null);
    }
}
