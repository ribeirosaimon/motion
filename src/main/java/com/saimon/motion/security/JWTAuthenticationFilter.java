package com.saimon.motion.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimon.motion.DTOs.LoginDTO;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.exception.ErrorResponse;
import com.saimon.motion.messagerResponse.ConstantMessager;
import com.saimon.motion.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserRepository userRepository;

    private String username;

    public JWTAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDTO loginDTO;
        try {
            loginDTO = new ObjectMapper().readValue(request.getReader(), LoginDTO.class);
            username = loginDTO.getUsername();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword(),
                Collections.emptyList());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        MotionPrincipal userDetails = (MotionPrincipal) authResult.getPrincipal();
        String token = JwtUtils.createToken(userDetails.getUsername(), userDetails.getRole());

        MotionUser motionUser = userRepository.findByUsername(username).get();

        if(motionUser.getStatus().equals(MotionUser.Status.INACTIVE)){
            this.setResponseError(response, ConstantMessager.ACCOUNT_INACTIVE);
        }
        if (!motionUser.getStatus().equals(MotionUser.Status.SUSPENDED) ||
                (motionUser.getLastLoginAttemp()
                        .after(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1))) || motionUser.getStatus().equals(MotionUser.Status.SUSPENDED))
        ) {
            motionUser.setLoginCount(motionUser.getLoginCount() + 1);
            motionUser.setLoginAttemp(0);
            if (motionUser.getStatus().equals(MotionUser.Status.SUSPENDED)) {
                motionUser.setStatus(MotionUser.Status.ACTIVE);
            }
            userRepository.save(motionUser);

            response.addHeader("Authorization", "Bearer " + token);
            response.getWriter().flush();

            super.successfulAuthentication(request, response, chain, authResult);
        } else {
            motionUser.setLastLoginAttemp(new Date());
            userRepository.save(motionUser);
            this.setResponseError(response, ConstantMessager.ACCOUNT_SUSPENDED);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        Optional<MotionUser> motionUser = userRepository.findByUsername(username);

        if (motionUser.isPresent()) {
            motionUser.get().setLastLoginAttemp(new Date());
            if (motionUser.get().getLoginAttemp() >= 10) {
                motionUser.get().setStatus(MotionUser.Status.SUSPENDED);
            } else {
                Integer loginAttemp = motionUser.get().getLoginAttemp();
                motionUser.get().setLoginAttemp(loginAttemp + 1);
            }
            userRepository.save(motionUser.get());
        }

        if (motionUser.get().getStatus().equals(MotionUser.Status.SUSPENDED)) {
            this.setResponseError(response, ConstantMessager.ACCOUNT_SUSPENDED);
            return;
        }
        this.setResponseError(response, ConstantMessager.INCORRECT_PASSWORD);
    }

    private void setResponseError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(message)));
    }
}
