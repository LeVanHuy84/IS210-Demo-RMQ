package io.messagequeue.server.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.messagequeue.server.model.auth.User;

public class AuthUtils {
    public static Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof User) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
    throw new RuntimeException("User not authenticated");
}


    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getAuthorities().isEmpty()) {
            return authentication.getAuthorities().iterator().next().getAuthority();
        }
        throw new RuntimeException("Role not found");
    }
}