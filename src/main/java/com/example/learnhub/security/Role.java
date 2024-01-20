package com.example.learnhub.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN(Code.ADMIN),
    COURSEMANAGER(Code.COURSEMANAGER),

    STUDENT(Code.STUDENT);

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public class Code {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String COURSEMANAGER = "ROLE_COURSEMANAGER";
        public static final String STUDENT = "ROLE_STUDENT";
    }
}
