package com.paychex.timeclock.core;

import lombok.Data;

@Data
public class ShiftRequest {
    private long requestBy;
    private long requestFor;
    private BreakType type;

    public boolean isSelf() {
        return this.requestBy == this.requestFor;
    }
}
