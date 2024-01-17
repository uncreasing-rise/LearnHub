package com.example.learnhub.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    SUPER_ADMIN(Code.SUPER_ADMIN),
    ADMIN(Code.ADMIN),

    TRAINER(Code.TRAINER);

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public class Code {
        public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String TRAINER = "ROLE_TRAINER";
    }
}
