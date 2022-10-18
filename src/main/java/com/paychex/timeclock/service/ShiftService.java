package java.com.paychex.timeclock.service;

import org.springframework.stereotype.Service;

import java.com.paychex.timeclock.core.Break;
import java.com.paychex.timeclock.core.Shift;
import java.com.paychex.timeclock.core.User;
import java.com.paychex.timeclock.repository.InMemoryRepository;
import java.time.Instant;
import java.util.UUID;

@Service
public class ShiftService {
    private UserService userService;
    private InMemoryRepository repository = InMemoryRepository.getInstance();

    public void startShift(long id, boolean admin) throws IllegalAccessException {
        User user = userService.getUser(id);
        Shift active = repository.findActiveShift(id);
        if (!(active == null || admin))
            throw new IllegalAccessException("Unable to start shift due to shift already started.");

        Shift shift = Shift.builder().uuid(UUID.randomUUID()).employee(user)
                .startTime(Instant.now()).createdBy(user).updatedBy(user).build();
        repository.save(shift);
    }

    public void endShift(long id, boolean admin) throws IllegalStateException {
        User user = userService.getUser(id);
        Shift active = repository.findActiveShift(id);
        if (active == null)
            throw new IllegalStateException("Unable to end shift.");

        active.setEndTime(Instant.now());
        repository.save(active);
    }

    public void startBreak(long id, boolean admin) throws IllegalAccessException {
        User user = userService.getUser(id);
        Shift activeShift = repository.findActiveShift(id);
        Break activeBreak = repository.findActiveBreak(id);
        if (!(activeShift != null && activeBreak == null || admin))
            throw new IllegalAccessException("Unable to start break due to break already started.");

        Break shift = Break.builder().uuid(UUID.randomUUID()).employee(user)
                .startTime(Instant.now()).createdBy(user).updatedBy(user).build();
        repository.save(shift);
    }

    public void endBreak(long id, boolean admin) throws IllegalStateException {
        User user = userService.getUser(id);
        Break activeBreak = repository.findActiveBreak(id);
        if (activeBreak == null)
            throw new IllegalStateException("Unable to end break.");

        activeBreak.setEndTime(Instant.now());
        repository.save(activeBreak);
    }
}
