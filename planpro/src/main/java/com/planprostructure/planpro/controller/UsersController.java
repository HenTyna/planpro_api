package com.planprostructure.planpro.controller;

import com.planprostructure.planpro.components.common.api.ProPlanRestController;
import com.planprostructure.planpro.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wb/v1/users")
@RequiredArgsConstructor
public class UsersController extends ProPlanRestController {
    private final UserService usersService;

    @GetMapping
    public Object getProfile() throws Throwable {
        return ok(usersService.getProfile());
    }
}
