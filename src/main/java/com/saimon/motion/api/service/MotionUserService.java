package com.saimon.motion.api.service;

import com.saimon.motion.DTOs.SignUpDTO;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MotionUserService {

    private final UserRepository userRepository;

    public MotionUser.MotionUserRef signUpUser(SignUpDTO newUserDTO) {
        if (userRepository.existsByUsername(newUserDTO.getUsername())){
            return null;
        }
        return null;
    }
}
