package com.paychex.timeclock.core;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    long id;
    String name;
    boolean admin;
}