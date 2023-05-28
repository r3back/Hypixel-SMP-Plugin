package me.reb4ck.smp.database.handler;

public interface DBHandler<T> {
    void saveDataRepeatedly(int minutes);

    void disable();

    void saveData(T object, boolean removeFromCache, boolean async);

    void loadData(T object);

    void deleteData(T object);
}
