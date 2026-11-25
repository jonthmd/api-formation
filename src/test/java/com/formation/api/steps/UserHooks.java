package com.formation.api.steps;

import com.formation.api.service.UserService;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class UserHooks {

    @Autowired
    private UserService userService;

//    @Before
//    public void resetData() {
//        userService.resetUsers();
//    }
}