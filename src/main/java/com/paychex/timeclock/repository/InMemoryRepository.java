package java.com.paychex.timeclock.repository;

import java.com.paychex.timeclock.core.Break;
import java.com.paychex.timeclock.core.Shift;
import java.com.paychex.timeclock.core.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository {
    private static InMemoryRepository repository = null;
    private final Map<Long, User> userTable;
    private final Map<Long, List<Shift>> shiftTable;
    private final Map<Long, List<Break>> breakTable;

    private InMemoryRepository() {
        this.userTable = new ConcurrentHashMap<>();

        this.shiftTable = new ConcurrentHashMap<>();
        this.breakTable = new ConcurrentHashMap<>();
    }

    public static InMemoryRepository getInstance() {
        if (repository == null)
            repository = new InMemoryRepository();

        return repository;
    }

    public List<User> findAllUsers() {
        return userTable.values().stream().toList();
    }

    public User findUserById(long id) {
        return userTable.get(id);
    }

    public User save(User user) {
        userTable.put(user.getId(), user);
        return userTable.get(user.getId());
    }

    public Shift findActiveShift(long id) {
        return shiftTable.get(id).stream()
                .filter(shift -> shift.getEndTime() == null).findFirst().orElse(null);
    }

    public Shift save(Shift shift) {
        List<Shift> userShifts = shiftTable.get(shift.getEmployee().getId());
        userShifts.add(shift);
        shiftTable.put(shift.getEmployee().getId(), userShifts);
        return userShifts.get(userShifts.size() - 1);
    }

    public Break findActiveBreak(long id) {
        return breakTable.get(id).stream()
                .filter(b -> b.getEndTime() == null).findFirst().orElse(null);
    }

    public Break save(Break b) {
        List<Break> userBreaks =  breakTable.get(b.getEmployee().getId());
        userBreaks.add(b);
        breakTable.put(b.getEmployee().getId(), userBreaks);
        return userBreaks.get(userBreaks.size() - 1);
    }
}
