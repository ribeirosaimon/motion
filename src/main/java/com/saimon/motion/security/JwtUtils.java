package com.saimon.motion.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JwtUtils {

    private static final String ACCESS_TOKEN = "Q4NSl604sgyHJj1qwEkR3ycUeR4uUAt7WJraD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4H";
    private static final Long EXPIRATION_TIME = TimeUnit.HOURS.toMillis(24);

    public static String createToken(UserDetails username) {
        Date expirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("username", username);

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, ACCESS_TOKEN.getBytes())
                .setSubject(username.getUsername())
                .setExpiration(expirationTime)
                .addClaims(extraInfo).compact();

    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(ACCESS_TOKEN)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
        } catch (JwtException e) {
            return null;
        }
    }
}
