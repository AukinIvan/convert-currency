package com.tz.convertercurrency.util;

import com.tz.convertercurrency.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    public static Long getUserId() {
        return getUser().getId();
    }
}
