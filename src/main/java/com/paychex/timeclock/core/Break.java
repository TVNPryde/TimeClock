package java.com.paychex.timeclock.core;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class Break {
    UUID uuid = UUID.randomUUID();
    User employee;
    Shift shift;
    BreakType type;
    Instant startTime;
    Instant endTime;
    User createdBy;
    User updatedBy;
}
