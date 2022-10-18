package com.paychex.timeclock.controller;

import com.paychex.timeclock.core.ReportResponse;
import com.paychex.timeclock.core.ShiftRequest;
import com.paychex.timeclock.core.User;
import com.paychex.timeclock.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/shift")
@RestController
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping("/starts")
    public ResponseEntity startShift(@RequestBody ShiftRequest request) {
        shiftService.startShift(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ends")
    public ResponseEntity endShift(@RequestBody ShiftRequest request) {
        shiftService.endShift(request.getRequestFor());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/break/starts")
    public ResponseEntity startBreak(@RequestBody ShiftRequest request) {
        shiftService.startBreak(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/break/ends")
    public ResponseEntity endBreak(@RequestBody Long id) {
        shiftService.endBreak(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/report/{id}")
    public ResponseEntity<ReportResponse> report(@PathVariable("id") long id) {
        return ResponseEntity.ok(shiftService.report(id));
    }
}
