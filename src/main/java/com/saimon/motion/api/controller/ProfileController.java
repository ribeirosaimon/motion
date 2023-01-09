package com.saimon.motion.api.controller;

import com.saimon.motion.api.service.ProfileService;
import com.saimon.motion.domain.Profile;
import com.saimon.motion.security.MotionLoggedUser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/my-profile")
    public Profile getMyProfile(MotionLoggedUser motionLoggedUser) {
        return profileService.getMyProfile(motionLoggedUser.getMotionUser());
    }
}

