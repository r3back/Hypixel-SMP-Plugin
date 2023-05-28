package me.reb4ck.smp.database.future;

import com.google.inject.ImplementedBy;

@ImplementedBy(SecondFutureImpl.class)
public interface SecondFuture extends FutureDatabase{
}
