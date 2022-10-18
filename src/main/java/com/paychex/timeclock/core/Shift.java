package com.paychex.timeclock.core;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class Shift {
    UUID uuid = UUID.randomUUID();
    User employee;
    Instant startTime;
    Instant endTime;
    User createdBy;
    User updatedBy;
}
