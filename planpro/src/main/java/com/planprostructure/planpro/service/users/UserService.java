package com.planprostructure.planpro.service.users;

import com.planprostructure.planpro.payload.users.UpdateProfileRequest;

public interface UserService {
    Object getProfile() throws Throwable;

    void updateProfile(UpdateProfileRequest paylod) throws Throwable;
}
