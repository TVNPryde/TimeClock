package java.com.paychex.timeclock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.com.paychex.timeclock.core.User;
import java.com.paychex.timeclock.service.UserService;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody() User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }
}
