package com.paychex.timeclock.service;

import com.paychex.timeclock.repository.InMemoryRepository;
import org.springframework.stereotype.Service;

import com.paychex.timeclock.core.User;

import java.util.List;

@Service
public class UserService {
    private InMemoryRepository repository = InMemoryRepository.getInstance();

    public User signup(String name) {
        return repository.save(User.builder().id(repository.getNextUserId())
                .name(name).admin(false).build());
    }

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
