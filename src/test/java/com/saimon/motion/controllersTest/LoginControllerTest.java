package com.saimon.motion.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.exception.ErrorResponse;
import com.saimon.motion.messagerResponse.ConstantMessager;
import com.saimon.motion.repository.AdminPromotionRepository;
import com.saimon.motion.repository.ProfileRepository;
import com.saimon.motion.repository.UserRepository;
import com.saimon.motion.util.UtilTest;
import org.junit.jupiter.api.*;
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
public class LoginControllerTest {
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
    @DisplayName("Login with Jwt is ok")
    public void existentUserCanGetTokenAndAuthentication() throws Exception {
        String activeMotionUser = utilTest.getActiveMotionUser().getUsername();
        String token = utilTest.makeLoginInApi(mvc, activeMotionUser);

        mvc.perform(MockMvcRequestBuilders.get("/ready")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("When I make login add loginCount")
    public void assertWhenMakeLoginAddOneLoginCount() throws Exception {
        MotionUser motionUser = userRepository.findByUsername(utilTest.getMotionUserInRepository().getUsername()).get();
        String token = utilTest.makeLoginInApi(mvc, null);
        Long correctLogin = userRepository.findByUsername(utilTest.getMotionUserInRepository().getUsername()).get().getLoginCount();
        Assertions.assertEquals(motionUser.getLoginCount(), correctLogin - 1);

        mvc.perform(MockMvcRequestBuilders.get("/ready")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("password incorrect and change user status to SUSPENDED")
    public void changeUserStatusToSuspended() throws Exception {
        MotionUser motionUser = userRepository.findByUsername(utilTest.getMotionUserInRepository().getUsername()).get();

        String username = motionUser.getUsername();
        String password = "";

        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        ErrorResponse errorResponse = new ErrorResponse(ConstantMessager.INCORRECT_PASSWORD);
        for (int i = 0; i < 10; i++) {
            mvc.perform(MockMvcRequestBuilders.post("/login")
                            .content(body))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(errorResponse)))
                    .andReturn();
        }
        errorResponse.setMessage(ConstantMessager.ACCOUNT_SUSPENDED);
        mvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(errorResponse)))
                .andReturn();

        Integer newLoginAttemp = userRepository.findByUsername(utilTest.getMotionUserInRepository().getUsername()).get().getLoginAttemp();
        Assertions.assertEquals(motionUser.getLoginAttemp() + 10, newLoginAttemp);
    }

}
