package com.saimon.motion.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimon.motion.DTOs.SignInDTO;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.exception.MotionException;
import com.saimon.motion.repository.AdminPromotionRepository;
import com.saimon.motion.repository.ProfileRepository;
import com.saimon.motion.repository.UserRepository;
import com.saimon.motion.util.UtilTest;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
//I use Junit4, caution for not use Junit5
public class UserControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminPromotionRepository adminPromotionRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Test
    @DisplayName("Sign Up with User return Created")
    public void signUpWithUser() throws Exception {
        UtilTest utilTest = new UtilTest(userRepository, adminPromotionRepository, profileRepository);
        String newName = "newName";
        SignInDTO signInDTO = utilTest.getSignInDTO(newName);

        String jsonSignIn = objectMapper.writeValueAsString(signInDTO);

        mvc.perform(MockMvcRequestBuilders.post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignIn))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("username").value(newName))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(newName));
    }

    @Test
    @DisplayName("User already exists return Exception")
    public void signUpAlreadyUser() throws Exception {
        UtilTest utilTest = new UtilTest(userRepository, adminPromotionRepository, profileRepository);
        SignInDTO signInDTO = utilTest.getSignInDTO();
        String jsonSignUp = objectMapper.writeValueAsString(signInDTO);
        mvc.perform(MockMvcRequestBuilders.post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignUp))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MotionException));
    }

    @Test
    @DisplayName("User must deactivate")
    public void userMustDeactivate() throws Exception {
        UtilTest utilTest = new UtilTest(userRepository, adminPromotionRepository, profileRepository);
        MotionUser motionUserInRepository = utilTest.getMotionUserInRepository();

        mvc.perform(MockMvcRequestBuilders.post("/inactive")
                        .header("Authorization", utilTest.getBearerToken(motionUserInRepository)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        MotionUser motionUserAfterController = utilTest.getMotionUserInRepository();
        Assertions.assertEquals(motionUserAfterController.getRole(), MotionUser.Role.USER);
        Assertions.assertEquals(motionUserAfterController.getStatus(), MotionUser.Status.INACTIVE);
    }

    @Test
    @DisplayName("Admin must deactivate")
    public void adminMustDeactivate() throws Exception {
        UtilTest utilTest = new UtilTest(userRepository, adminPromotionRepository, profileRepository);
        MotionUser motionAdminInRepository = utilTest.getMotionAdminInRepository();

        mvc.perform(MockMvcRequestBuilders.post("/inactive")
                        .header("Authorization", utilTest.getBearerToken(motionAdminInRepository)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        MotionUser motionAdminInController = utilTest.getMotionAdminInRepository();
        Assertions.assertEquals(motionAdminInController.getRole(), MotionUser.Role.ADMIN);
        Assertions.assertEquals(motionAdminInController.getStatus(), MotionUser.Status.INACTIVE);
    }

    @Test
    @DisplayName("Admin must to ban a user")
    public void adminMustToBanAUser() throws Exception {
        UtilTest utilTest = new UtilTest(userRepository, adminPromotionRepository, profileRepository);
        MotionUser motionAdminInRepository = utilTest.getMotionAdminInRepository();
        String activeUser = utilTest.getActiveMotionUser().getUsername();
        MotionUser motionUserInRepository = userRepository.findByUsername(activeUser).get();

        mvc.perform(MockMvcRequestBuilders.post(String.format("/inactive/%s", motionUserInRepository.getId()))
                        .header("Authorization", utilTest.getBearerToken(motionAdminInRepository)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        MotionUser motionUserInController = utilTest.getMotionUserInRepository();

        Assertions.assertEquals(motionUserInController.getRole(), MotionUser.Role.USER);
        Assertions.assertEquals(motionUserInController.getStatus(), MotionUser.Status.INACTIVE);
    }


    @Test
    @DisplayName("Admin make user Admin")
    public void adminMakeUserToAdmin() throws Exception {
        UtilTest utilTest = new UtilTest(userRepository, adminPromotionRepository, profileRepository);
        MotionUser motionAdminInRepository = utilTest.getMotionAdminInRepository();
        String activeUser = utilTest.getActiveMotionUser().getUsername();
        MotionUser motionUserInRepository = userRepository.findByUsername(activeUser).get();

        mvc.perform(MockMvcRequestBuilders.post(String.format("/promotion/%s", motionUserInRepository.getId()))
                        .header("Authorization", utilTest.getBearerToken(motionAdminInRepository)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        MotionUser motionUserInController = userRepository.findByUsername(activeUser).get();

        Assertions.assertEquals(motionUserInController.getRole(), MotionUser.Role.ADMIN);
        Assertions.assertEquals(motionUserInController.getStatus(), MotionUser.Status.ACTIVE);
    }
}

