package me.reb4ck.smp.markable;

public interface Markable {
    boolean isMarked();

    void mark();

    long remainingTime();

    void setDelay(long delay);

    long getDelay();
}