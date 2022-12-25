package com.saimon.motion.security;

import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MotionUser motionUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserDetailsImpl(motionUser);
    }
}
