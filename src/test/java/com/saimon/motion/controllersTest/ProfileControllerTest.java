package com.saimon.motion.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.repository.AdminPromotionRepository;
import com.saimon.motion.repository.ProfileRepository;
import com.saimon.motion.repository.UserRepository;
import com.saimon.motion.util.UtilTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProfileControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AdminPromotionRepository adminPromotionRepository;
    @Autowired
    ProfileRepository profileRepository;
    UtilTest utilTest;

    @BeforeAll
    public void setUp() {
        utilTest = new UtilTest(userRepository, adminPromotionRepository, profileRepository);
        utilTest.saveMotionUserAndAdmin();
    }

    @Test
    @DisplayName("Find my Profile")
    public void findMyProfileTest() throws Exception {
        MotionUser activeMotionUser = utilTest.getActiveMotionUser();
        String token = utilTest.makeLoginInApi(mvc, activeMotionUser.getUsername());

        mvc.perform(MockMvcRequestBuilders.get("/profile/my-profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(activeMotionUser.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("birthday").value(activeMotionUser.getBirthday()));
    }
}
