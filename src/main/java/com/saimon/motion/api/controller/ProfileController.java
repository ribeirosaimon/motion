package com.saimon.motion.api.controller;

import com.saimon.motion.api.service.ProfileService;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.security.MotionLoggedUser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController("profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/my-profile")
    public MotionUser.MotionUserRef getMyProfile(MotionLoggedUser motionLoggedUser) {
        return profileService.getMyProfile(motionLoggedUser.getMotionUser());
    }
}

