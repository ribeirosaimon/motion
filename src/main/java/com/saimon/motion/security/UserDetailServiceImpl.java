package com.saimon.motion.security;

import com.saimon.motion.domain.User;
import com.saimon.motion.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional
                .ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("this user does not exists"));

        List<GrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().forEach(e -> authorities.add(new SimpleGrantedAuthority(e.name())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
//    https://www.youtube.com/watch?v=atudlWCV1FU
}
