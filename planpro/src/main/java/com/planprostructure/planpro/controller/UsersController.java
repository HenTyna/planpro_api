package com.planprostructure.planpro.controller;

import com.planprostructure.planpro.components.common.api.ProPlanRestController;
import com.planprostructure.planpro.payload.users.UpdateProfileRequest;
import com.planprostructure.planpro.service.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wb/v1/users")
@RequiredArgsConstructor
public class UsersController extends ProPlanRestController {
    private final UserService usersService;

    @GetMapping
    public Object getProfile() throws Throwable {
        return ok(usersService.getProfile());
    }

    @PatchMapping
    public Object updateProfile(@Valid @RequestBody  UpdateProfileRequest paylod) throws Throwable {
        usersService.updateProfile(paylod);
        return ok();
    }
}
