package com.saimon.motion.util;

import com.saimon.motion.DTOs.SignInDTO;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.domain.Role;
import com.saimon.motion.repository.RoleRepository;
import com.saimon.motion.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UtilTest {

    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "teste";
    private static final String CPF = "12345678901";
    private static final String PHONE = "11222223333";
    private static final String PASSWORD_ENCODED = "$2a$10$y7BaCXMYDE6xkJl76bKedewKKlz2vd80dPQM9AFxS6OaBaqu2PK6S";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UtilTest(UserRepository userRepository, RoleRepository roleRepository) throws Exception {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.saveMotionUser();
    }

    public MotionUser getMotionUserInRepository() {
        return userRepository.findByUsername(USERNAME).get();
    }

    public SignInDTO getSignInDTO() {
        return this.getSignInDTO(USERNAME);
    }

    public SignInDTO getSignInDTO(String username) {
        SignInDTO signInDTO = new SignInDTO();
        signInDTO.setName(username);
        signInDTO.setUsername(username);
        signInDTO.setPassword(PASSWORD);
        signInDTO.setCpf(CPF);
        signInDTO.setPhone(PHONE);

        return signInDTO;
    }

    public MotionUser createUser(String username) {
        Role role = this.roleRepository.findByName("USER").get();

        MotionUser motionUser = new MotionUser();
        motionUser.setUsername(username);
        motionUser.setName(username);
        motionUser.setPassword(PASSWORD_ENCODED);
        motionUser.setCpf(CPF);
        motionUser.setPhone(PHONE);
        motionUser.setStatus(MotionUser.Status.ACTIVE);
        motionUser.setLoginCount(0L);
        motionUser.setLoginAttemp(0);
        motionUser.setLastLoginAttemp(new Date());
        motionUser.setRoles(List.of(role));

        return motionUser;
    }

    public void saveMotionUser() throws Exception {
        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        Role userRole = new Role();
        userRole.setName("USER");
        if (!this.userRepository.existsByUsername(USERNAME)) {
            this.roleRepository.save(adminRole);
            Role role = this.roleRepository.save(userRole);

            MotionUser motionUser = new MotionUser();

            motionUser.setUsername(USERNAME);
            motionUser.setName(USERNAME);
            motionUser.setPassword(PASSWORD_ENCODED);
            motionUser.setCpf(CPF);
            motionUser.setPhone(PHONE);
            motionUser.setStatus(MotionUser.Status.ACTIVE);
            motionUser.setLoginCount(0L);
            motionUser.setLoginAttemp(0);
            motionUser.setLastLoginAttemp(new Date());
            motionUser = this.userRepository.save(motionUser);
            motionUser.setRoles(Collections.singletonList(role));
            this.userRepository.save(motionUser);
        }
    }

    public String loginBody() {
        return "{\"username\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}";
    }
}
