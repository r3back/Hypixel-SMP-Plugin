package me.reb4ck.smp.base.placeholder;

import me.reb4ck.smp.api.placeholder.IPlaceholder;

public final class Placeholder implements IPlaceholder {
    private final String key;
    private final String value;

    public Placeholder(String key, String value) {
        this.key = "%" + key + "%";
        this.value = value;
    }

    public Placeholder(String key, int value) {
        this.key = "%" + key + "%";
        this.value = String.valueOf(value);
    }

    public Placeholder(String key, double value) {
        this.key = "%" + key + "%";
        this.value = String.valueOf((int) value);
    }

    @Override
    public final String process(String line) {
        if (line == null)
            return "";
        return line.replace(this.key, this.value);
    }
}