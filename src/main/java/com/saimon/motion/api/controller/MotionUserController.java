package com.saimon.motion.api.controller;

import com.saimon.motion.DTOs.SignInDTO;
import com.saimon.motion.api.service.MotionUserService;
import com.saimon.motion.domain.MotionUser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MotionUserController {

    private final MotionUserService motionUserService;

    @PostMapping("/signin")
    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MotionUser.MotionUserRef saveMotionUser(@RequestBody SignInDTO newUserDTO) throws Exception {
        return motionUserService.signUpUser(newUserDTO);
    }
}
