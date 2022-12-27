package com.saimon.motion.api.controller;

import com.saimon.motion.DTOs.SignUpDTO;
import com.saimon.motion.api.service.MotionUserService;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.security.MotionLoggedUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MotionUserController {

    private final MotionUserService motionUserService;

    @PostMapping("/signup")
    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MotionUser.MotionUserRef saveMotionUser(@RequestBody SignUpDTO newUserDTO) {
        return motionUserService.signUpUser(newUserDTO);
    }

}
