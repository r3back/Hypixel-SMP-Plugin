package me.reb4ck.smp.base.markable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.reb4ck.smp.markable.Markable;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class MarkableImpl implements Markable {
    protected long delay;
    protected long lastMarked;

    public MarkableImpl(long delay, long lastMarked) {
        this.delay = delay;
        this.lastMarked = lastMarked;
    }

    @JsonIgnore
    public boolean isMarked(){
        return lastMarked + delay - System.currentTimeMillis() >= 0;
    }

    @JsonIgnore
    public void mark(){
        lastMarked = System.currentTimeMillis();
    }

    @JsonIgnore
    public long remainingTime(){
        return lastMarked + delay - System.currentTimeMillis();
    }
}
