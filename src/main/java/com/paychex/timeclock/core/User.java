package java.com.paychex.timeclock.core;

import lombok.Data;

@Data
public class User {
    long id;
    String name;
    UserType privelige;

    public boolean isAdmin() {
        return this.privelige == UserType.ADMIN;
    }
}