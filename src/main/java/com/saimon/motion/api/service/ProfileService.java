package com.saimon.motion.api.service;

import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.domain.Profile;
import com.saimon.motion.exception.MotionException;
import com.saimon.motion.repository.ProfileRepository;
import com.saimon.motion.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    public Profile getMyProfile(MotionUser.MotionUserRef motionUserRef) {
        if (!userRepository.existsById(motionUserRef.getId())) {
            throw new MotionException("User not found");
        }
        return profileRepository.findProfileByMotionUser_Id(motionUserRef.getId()).orElseThrow(() -> new MotionException("Profile not found"));
    }

}
