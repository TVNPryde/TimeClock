package com.paychex.timeclock.core;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ReportResponse {
    User employee;
    List<ShiftReport> shifts;
}
