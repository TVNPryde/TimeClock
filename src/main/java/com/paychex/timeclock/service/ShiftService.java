package com.paychex.timeclock.service;

import com.paychex.timeclock.core.*;
import com.paychex.timeclock.repository.InMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final UserService userService;
    private InMemoryRepository repository = InMemoryRepository.getInstance();

    public void startShift(ShiftRequest request) {
        User forUser = userService.getUser(request.getRequestFor());
        User byUser = request.isSelf() ? forUser : userService.getUser(request.getRequestBy());
        Shift active = repository.findActiveShift(forUser.getId());
        if (!(active == null || byUser.isAdmin()))
            throw new RuntimeException("Unable to start shift due to shift already started.");

        Shift shift = Shift.builder().uuid(UUID.randomUUID()).employee(forUser)
                .startTime(Instant.now()).createdBy(byUser).updatedBy(byUser).build();
        repository.save(shift);
    }

    public void endShift(long id) {
        Shift active = repository.findActiveShift(id);
        if (active == null)
            throw new RuntimeException("Unable to end shift.");

        active.setEndTime(Instant.now());
        repository.save(active);
    }

    public void startBreak(ShiftRequest request) {
        User forUser = userService.getUser(request.getRequestFor());
        User byUser = request.isSelf() ? forUser : userService.getUser(request.getRequestBy());
        Shift activeShift = repository.findActiveShift(forUser.getId());
        Break activeBreak = repository.findActiveBreak(forUser.getId());
        if (!(activeShift != null || byUser.isAdmin()))
            throw new RuntimeException("Unable to start break due to no shift started.");

        if (!(activeShift != null && activeBreak == null) || byUser.isAdmin())
            throw new RuntimeException("Unable to start break due to break already started.");

        Break shift = Break.builder().uuid(UUID.randomUUID()).employee(forUser).shift(activeShift).type(request.getType())
                .startTime(Instant.now()).createdBy(byUser).updatedBy(byUser).build();
        repository.save(shift);
    }

    public void endBreak(long id) {
        Break activeBreak = repository.findActiveBreak(id);
        if (activeBreak == null)
            throw new RuntimeException("Unable to end break.");

        activeBreak.setEndTime(Instant.now());
        repository.save(activeBreak);
    }

    public ReportResponse report(long id) {
        User user = userService.getUser(id);
        List<Shift> shiftList = repository.getUserShiftReport(id);
        List<Break> breaks = repository.getUserBreakReport(id);

        List<ShiftReport> shiftReports = shiftList.stream().map(shift -> {
            List<BreakReport> shiftBreaks = breaks.stream()
                    .filter(aBreak ->
                            shift.getStartTime().isBefore(aBreak.getStartTime())
                            && shift.getEndTime() != null ? shift.getEndTime().isAfter(aBreak.getStartTime()) : true)
                    .map(b -> {
                        return BreakReport.builder()
                                .uuid(b.getUuid())
                                .type(b.getType())
                                .startTime(b.getStartTime())
                                .endTime(b.getEndTime())
                                .createdBy(b.getCreatedBy().getName())
                                .updatedBy(b.getUpdatedBy().getName())
                                .build();
                    }).toList();

            return ShiftReport.builder()
                    .uuid(shift.getUuid())
                    .startTime(shift.getStartTime())
                    .endTime(shift.getEndTime())
                    .createdBy(shift.getCreatedBy().getName())
                    .updatedBy(shift.getUpdatedBy().getName())
                    .breaks(shiftBreaks)
                    .build();
        }).toList();

        return ReportResponse.builder().employee(user).shifts(shiftReports).build();
    }
}
