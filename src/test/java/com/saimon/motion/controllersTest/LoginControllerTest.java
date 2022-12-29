package com.saimon.motion.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.exception.ErrorResponse;
import com.saimon.motion.messagerResponse.ConstantMessager;
import com.saimon.motion.repository.RoleRepository;
import com.saimon.motion.repository.UserRepository;
import com.saimon.motion.util.UtilTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
    RoleRepository roleRepository;
    @Autowired
    ObjectMapper objectMapper;
    UtilTest utilTest;

    @BeforeAll
    public void setUp() throws Exception {
        utilTest = new UtilTest(userRepository, roleRepository);
        utilTest.saveMotionUser();
    }

    private String makeLoginInApi() throws Exception {
        String body = utilTest.loginBody();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        return result.getResponse().getHeader("Authorization");
    }

    @Test
    @DisplayName("Login with Jwt is ok")
    public void existentUserCanGetTokenAndAuthentication() throws Exception {
        String token = this.makeLoginInApi();

        mvc.perform(MockMvcRequestBuilders.get("/ready")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("When I make login add loginCount")
    public void assertWhenMakeLoginAddOneLoginCount() throws Exception {
        MotionUser motionUser = userRepository.findByUsername(utilTest.getMotionUserInRepository().getUsername()).get();
        String token = this.makeLoginInApi();
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
