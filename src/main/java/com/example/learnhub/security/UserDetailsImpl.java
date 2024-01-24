package com.example.learnhub.security;

import com.example.learnhub.Entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
    private int id;
    private String email;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }


    public UserDetailsImpl(int id, String email, String password, List<Role> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = roles;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user.getUserId(), user.getEmail(), user.getUserPassword());
    }


    public static UserDetailsImpl build(User user, String role) {
        List<Role> roleList  = new ArrayList<>();
        roleList.add(Role.findByAbbr(role));
        return new UserDetailsImpl(user.getUserId(), user.getEmail(), user.getUserPassword(),roleList);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public Integer getId() {
        return this.id;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
