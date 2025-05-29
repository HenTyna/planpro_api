package com.planprostructure.planpro.helper;

import com.planprostructure.planpro.domain.security.SecurityUser;
import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.domain.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthHelper {

    public static Authentication getAuth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getToken() {
        Jwt jwt = (Jwt) getAuth().getPrincipal();
        return jwt.getTokenValue();
    }

    public static Jwt getJwt(){
        if(getAuth().getPrincipal() instanceof Jwt){
            return (Jwt) getAuth().getPrincipal();
        }
        return null;
    }

    public static SecurityUser getSecurityUser(){
        var authentication = getAuth();

        if(authentication.getPrincipal() instanceof SecurityUser){
            return ((SecurityUser) getAuth().getPrincipal());
        }

        return null;
    }

    public static Users getUser() {
        SecurityUser securityUser = getSecurityUser();
        if (securityUser == null) {
            throw new UsernameNotFoundException("SecurityUser is null");
        }
        return securityUser.users();
    }

    public static Long getCurrentUserId() {
        var userId = getUser().getId();
        if (getUser().getId() != null) {
             userId = getUser().getId();
        } else {
            throw new UsernameNotFoundException("User ID is null");
        }
        return userId;
    }

    public String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        }

        return principal.toString();
    }
}