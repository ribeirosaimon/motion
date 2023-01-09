package com.saimon.motion.util;

import com.saimon.motion.DTOs.SignInDTO;
import com.saimon.motion.domain.AdminPromotion;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.domain.Profile;
import com.saimon.motion.repository.AdminPromotionRepository;
import com.saimon.motion.repository.ProfileRepository;
import com.saimon.motion.repository.UserRepository;
import com.saimon.motion.security.JwtUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    private final ProfileRepository profileRepository;

    public UtilTest(UserRepository userRepository, AdminPromotionRepository adminPromotionRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.adminPromotionRepository = adminPromotionRepository;
        this.profileRepository = profileRepository;
        this.saveMotionUserAndAdmin();
        this.saveOtherUsers();
    }

    private void saveOtherUsers() {
        for (int i = 0; i > 3; i++) {
            MotionUser user = createUser(String.format(USERNAME + i));
            userRepository.save(user);
        }
    }

    public MotionUser getActiveMotionUser() {
        List<MotionUser> collect = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(s -> s.getStatus().equals(MotionUser.Status.ACTIVE)).toList();

        if (collect.size() == 0) {
            MotionUser activateUser = this.createUser("activateUser");
            return userRepository.save(activateUser);
        }

        return collect.get(0);
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

    public Profile createProfile(MotionUser motionUser){
        Profile profile = new Profile();
        profile.setName(motionUser.getName());
        profile.setBirthday(motionUser.getBirthday());
        profile.setSharedBy(Profile.SharedBy.ME);
        return profile;
    }

    public String getBearerToken(MotionUser motionUserInRepository) {
        String token = JwtUtils.createToken(motionUserInRepository.getUsername(), motionUserInRepository.getRole());
        return String.format("Bearer %s", token);
    }

    public void saveMotionUserAndAdmin() {
        if (!this.userRepository.existsByUsername(USERNAME)) {
            MotionUser motionUser = this.createUser(USERNAME);
            Profile profile = this.createProfile(motionUser);
            MotionUser saveMotionUser = this.userRepository.save(motionUser);
            profile.setMotionUser(saveMotionUser);
            this.profileRepository.save(profile);
        }
        if (!this.userRepository.existsByUsername(ADMIN_USERNAME)) {
            MotionUser motionAdmin = this.createUser(ADMIN_USERNAME);
            motionAdmin.setRole(MotionUser.Role.ADMIN);

            AdminPromotion adminPromotion = new AdminPromotion();
            adminPromotion.setMotionUser(motionAdmin);
            adminPromotion.setCountPromotionUser(0);
            adminPromotion.setCountBanUser(0);
            adminPromotion.setPromotedAt(new Date());
            MotionUser saveMotionAdmin = this.userRepository.save(motionAdmin);
            Profile profile = this.createProfile(saveMotionAdmin);
            this.profileRepository.save(profile);
            this.adminPromotionRepository.save(adminPromotion);
        }
    }

    public String loginBody(String username) {
        return "{\"username\":\"" + username + "\", \"password\":\"" + PASSWORD + "\"}";
    }

    public String loginBody() {
        return "{\"username\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}";
    }

    public String makeLoginInApi(MockMvc mvc, String username) throws Exception {

        String body;
        if (username == null) {
            body = this.loginBody();
        } else {
            body = this.loginBody(username);
        }

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String resultString = result.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("token").toString();
    }
}
