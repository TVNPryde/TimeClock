package com.paychex.timeclock.repository;

import com.paychex.timeclock.core.Break;
import com.paychex.timeclock.core.Shift;
import com.paychex.timeclock.core.User;

import java.util.ArrayList;
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
        User initialUser = User.builder().name("Thanh").id(1).admin(true).build();
        this.userTable.put(initialUser.getId(), initialUser);

        this.shiftTable = new ConcurrentHashMap<>();
        this.breakTable = new ConcurrentHashMap<>();
    }

    public static InMemoryRepository getInstance() {
        if (repository == null)
            repository = new InMemoryRepository();

        return repository;
    }

    public long getNextUserId() {
        return userTable.size() + 1;
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
        if (shiftTable.containsKey(id))
            return shiftTable.get(id).stream()
                .filter(shift -> shift.getEndTime() == null).findFirst().orElse(null);

        return null;
    }

    public Shift save(Shift shift) {
        List<Shift> userShifts = shiftTable.get(shift.getEmployee().getId());
        if (userShifts == null)
            userShifts = new ArrayList<>();

        Optional<Shift> exist = userShifts.stream().filter(s -> s.getUuid().equals(shift.getUuid())).findFirst();
        if (exist.isPresent()) {
            exist.get().setEndTime(shift.getEndTime());
            exist.get().setUpdatedBy(shift.getUpdatedBy());
        } else {
            userShifts.add(shift);
        }
        shiftTable.put(shift.getEmployee().getId(), userShifts);
        return userShifts.get(userShifts.size() - 1);
    }

    public Break findActiveBreak(long id) {
        if (breakTable.containsKey(id))
            return breakTable.get(id).stream()
                .filter(b -> b.getEndTime() == null).findFirst().orElse(null);

        return null;
    }

    public Break save(Break b) {
        List<Break> userBreaks =  breakTable.get(b.getEmployee().getId());
        if (userBreaks == null)
            userBreaks = new ArrayList<>();

        Optional<Break> exist = userBreaks.stream().filter(br -> br.getUuid().equals(b.getUuid())).findFirst();
        if (exist.isPresent()) {
            exist.get().setEndTime(b.getEndTime());
            exist.get().setUpdatedBy(b.getUpdatedBy());
        } else {
            userBreaks.add(b);
        }
        breakTable.put(b.getEmployee().getId(), userBreaks);
        return userBreaks.get(userBreaks.size() - 1);
    }

    public List<Shift> getUserShiftReport(long id) {
        return shiftTable.get(id);
    }

    public List<Break> getUserBreakReport(long id) {
        return breakTable.get(id);
    }
}
