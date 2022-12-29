package com.saimon.motion.security;

import com.saimon.motion.domain.MotionUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class JwtUtils {

    private static final String ACCESS_TOKEN = "Q4NSl604sgyHJj1qwEkR3ycUeR4uUAt7WJraD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4H";
    private static final Long EXPIRATION_TIME = TimeUnit.HOURS.toMillis(24);

    public static String createToken(String username, MotionUser.Role role) {
        Date expirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("roles", role);

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, ACCESS_TOKEN.getBytes())
                .setSubject(username)
                .setExpiration(expirationTime)
                .addClaims(extraInfo).compact();

    }

    public static MotionLoggedUser getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(ACCESS_TOKEN.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            String role = (String) claims.get("roles");
            List<GrantedAuthority> rolesAuthority = new ArrayList<>();
            rolesAuthority.add(new SimpleGrantedAuthority("ROLE_" + role));

            return new MotionLoggedUser(username, null, rolesAuthority);
        } catch (JwtException e) {
            return null;
        }
    }
}
