package com.example.learnhub.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.learnhub.Entity.User;
import com.example.learnhub.security.UserDetailsImpl;
import org.apache.groovy.parser.antlr4.GroovyLexer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JWTUtils {


    public static String generateAccessToken(UserDetailsImpl userDetails) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
            .withSubject(userDetails.getUsername())
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(new Date().getTime() + 86400000))
            .withClaim("id", userDetails.getId())
            .sign(algorithm);
    }


    public static String generateAccessToken(UserDetailsImpl userDetails, User user) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
            .withSubject(userDetails.getUsername())
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(new Date().getTime() + 86400000))
            .withClaim("id", userDetails.getId())
            .withClaim("email", user.getEmail())
            .withClaim("name", user.getFullName())
            .withClaim("role", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .sign(algorithm);
    }

    public static String generateRefreshToken(UserDetailsImpl userDetails) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
            .withSubject(userDetails.getUsername())
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(new Date().getTime() + 5 * 86400000))
            .sign(algorithm);
    }

}
