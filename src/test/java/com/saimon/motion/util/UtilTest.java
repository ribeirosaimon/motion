package com.saimon.motion.util;

import com.saimon.motion.DTOs.SignInDTO;
import com.saimon.motion.domain.AdminPromotion;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.repository.AdminPromotionRepository;
import com.saimon.motion.repository.UserRepository;
import com.saimon.motion.security.JwtUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

@Getter
@Setter
public class UtilTest {

    private static final String USERNAME = "testUsername";
    private static final String ADMIN_USERNAME = "testAdminUsername";
    private static final String PASSWORD = "teste";
    private static final MotionUser.Gender GENDER = MotionUser.Gender.HETERO;
    private static final String PHONE = "11222223333";
    private static final String PASSWORD_ENCODED = "$2a$10$y7BaCXMYDE6xkJl76bKedewKKlz2vd80dPQM9AFxS6OaBaqu2PK6S";
    private final UserRepository userRepository;
    private final AdminPromotionRepository adminPromotionRepository;

    public UtilTest(UserRepository userRepository, AdminPromotionRepository adminPromotionRepository) {
        this.userRepository = userRepository;
        this.adminPromotionRepository = adminPromotionRepository;
        this.saveMotionUserAndAdmin();
        this.saveOtherUsers();
    }

    private void saveOtherUsers() {
        for (int i = 0; i > 3; i++) {
            MotionUser user = createUser(String.format(USERNAME + i));
            userRepository.save(user);
        }
    }

    public String getActiveMotionUser() {
        List<MotionUser> collect = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(s -> s.getStatus().equals(MotionUser.Status.ACTIVE)).toList();

        if (collect.size() == 0) {
            MotionUser activateUser = this.createUser("activateUser");
            return userRepository.save(activateUser).getUsername();
        }

        return collect.get(0).getUsername();
    }

    public MotionUser getMotionUserInRepository() {
        return userRepository.findByUsername(USERNAME).get();
    }

    public MotionUser getMotionAdminInRepository() {
        return userRepository.findByUsername(ADMIN_USERNAME).get();
    }

    public SignInDTO getSignInDTO() {
        return this.getSignInDTO(USERNAME);
    }

    public SignInDTO getSignInDTO(String username) {
        SignInDTO signInDTO = new SignInDTO();
        signInDTO.setName(username);
        signInDTO.setUsername(username);
        signInDTO.setPassword(PASSWORD);
        signInDTO.setGender(GENDER);
        signInDTO.setPhone(PHONE);

        return signInDTO;
    }

    public MotionUser createUser(String username) {
        MotionUser motionUser = new MotionUser();
        motionUser.setUsername(username);
        motionUser.setName(username);
        motionUser.setPassword(PASSWORD_ENCODED);
        motionUser.setGender(GENDER);
        motionUser.setPhone(PHONE);
        motionUser.setStatus(MotionUser.Status.ACTIVE);
        motionUser.setLoginCount(0L);
        motionUser.setLoginAttemp(0);
        motionUser.setLastLoginAttemp(new Date());
        motionUser.setRole(MotionUser.Role.USER);

        return motionUser;
    }

    public MotionUser createAdmin() {
        MotionUser admin = this.createUser(ADMIN_USERNAME);
        admin.setRole(MotionUser.Role.ADMIN);
        return admin;
    }

    public String getBearerToken(MotionUser motionUserInRepository) {
        String token = JwtUtils.createToken(motionUserInRepository.getUsername(), motionUserInRepository.getRole());
        return String.format("Bearer %s", token);
    }

    public void saveMotionUserAndAdmin() {
        if (!this.userRepository.existsByUsername(USERNAME)) {
            MotionUser motionUser = this.createUser(USERNAME);
            this.userRepository.save(motionUser);
        }
        if (!this.userRepository.existsByUsername(ADMIN_USERNAME)) {
            MotionUser motionAdmin = this.createUser(ADMIN_USERNAME);
            motionAdmin.setRole(MotionUser.Role.ADMIN);

            AdminPromotion adminPromotion = new AdminPromotion();
            adminPromotion.setMotionUser(motionAdmin);
            adminPromotion.setCountPromotionUser(0);
            adminPromotion.setCountBanUser(0);
            adminPromotion.setPromotedAt(new Date());
            this.userRepository.save(motionAdmin);
            this.adminPromotionRepository.save(adminPromotion);
        }
    }

    public String loginBody(String username) {
        return "{\"username\":\"" + username + "\", \"password\":\"" + PASSWORD + "\"}";
    }

    public String loginBody() {
        return "{\"username\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}";
    }
}
