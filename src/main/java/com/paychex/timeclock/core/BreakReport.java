package com.paychex.timeclock.core;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class BreakReport {
    UUID uuid = UUID.randomUUID();
    BreakType type;
    Instant startTime;
    Instant endTime;
    String createdBy;
    String updatedBy;
}
