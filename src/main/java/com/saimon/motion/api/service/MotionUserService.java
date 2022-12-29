package com.saimon.motion.api.service;

import com.saimon.motion.DTOs.SignInDTO;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.domain.Role;
import com.saimon.motion.exception.MotionException;
import com.saimon.motion.repository.RoleRepository;
import com.saimon.motion.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class MotionUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public MotionUser.MotionUserRef signUpUser(SignInDTO newUserDTO) throws Exception {

        if (userRepository.existsByUsername(newUserDTO.getUsername())) {
            throw new MotionException("User already exists");
        }

        MotionUser motionUser = new MotionUser();
        motionUser.setName(newUserDTO.getName());
        motionUser.setUsername(newUserDTO.getUsername());
        motionUser.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
        motionUser.setPhone(newUserDTO.getPhone());
        motionUser.setCpf(newUserDTO.getCpf());
        motionUser.setLoginAttemp(0);
        motionUser.setLoginCount(0L);
        motionUser.setStatus(MotionUser.Status.ACTIVE);
        Role role = roleRepository.findByName("USER").orElseThrow(Exception::new);
        motionUser.setRoles(Collections.singletonList(role));

        return userRepository.save(motionUser).getUserRef();
    }

    public void inactiveUser(Long userId) {
        MotionUser motionUser = userRepository.findById(userId).orElseThrow(() -> new MotionException("User not found"));

    }
}
