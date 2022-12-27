package com.saimon.motion.controllersTest;

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

    @BeforeAll
    public void setUp() {
        UtilTest.saveMotionUser(userRepository);
    }

    @Test
    @DisplayName("Login with Jwt is ok")
    public void existentUserCanGetTokenAndAuthentication() throws Exception {
        String body = UtilTest.loginBody();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String token = result.getResponse().getHeader("Authorization");

        mvc.perform(MockMvcRequestBuilders.get("/ready")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Not exists user for login")
    public void notExistUserToLogin() throws Exception {
        String username = "meuTeste";
        String password = "";

        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        mvc.perform(MockMvcRequestBuilders.post("/ready")
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()).andReturn();
    }
}
