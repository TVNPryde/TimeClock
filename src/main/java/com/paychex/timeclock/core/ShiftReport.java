package com.paychex.timeclock.core;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class ShiftReport {
    UUID uuid;
    Instant startTime;
    Instant endTime;
    String createdBy;
    String updatedBy;
    List<BreakReport> breaks;
}
