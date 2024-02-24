package com.example.learnhub.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;

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

    public static class Code {
        public static final String ADMIN = "Admin";
        public static final String COURSEMANAGER = "CourseManager";
        public static final String STUDENT = "Student";
    }


//    public static final Role getByValue(String value){
//        return Arrays.stream(Role.values()).filter(enumRole -> enumRole.authority.equals(value)).findFirst().orElse(null);
//    }


    public static Role findByAbbr(String abbr){
        for(Role v : values()){
            if( v.name().equalsIgnoreCase(abbr)){
                return v;
            }
        }
        return null;
    }
}
