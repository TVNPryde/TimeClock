package java.com.paychex.timeclock.service;

import org.springframework.stereotype.Service;

import java.com.paychex.timeclock.core.User;
import java.com.paychex.timeclock.repository.InMemoryRepository;
import java.util.List;

@Service
public class UserService {
    private InMemoryRepository repository = InMemoryRepository.getInstance();

    public List<User> getAll() {
        return repository.findAllUsers();
    }

    public User getUser(long id) {
        return repository.findUserById(id);
    }

    public User saveUser(User user) {
        return repository.save(user);
    }
}
