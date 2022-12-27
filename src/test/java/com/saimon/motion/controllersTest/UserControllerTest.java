package com.saimon.motion.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimon.motion.DTOs.SignUpDTO;
import com.saimon.motion.exception.MotionException;
import com.saimon.motion.repository.UserRepository;
import com.saimon.motion.util.UtilTest;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @BeforeAll
    public void setUp(){
        UtilTest.saveMotionUser(userRepository);
    }
    @Test
    @DisplayName("Sign Up with User return Created")
    public void signUpWithUser() throws Exception {
        SignUpDTO signUpDTO = UtilTest.getSignUpDTO();
        String jsonSignUp = objectMapper.writeValueAsString(signUpDTO);
        mvc.perform(MockMvcRequestBuilders.post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonSignUp))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("User already exists return Exception")
    public void signUpAlreadyUser() throws Exception {
        SignUpDTO signUpDTO = UtilTest.getSignUpDTO();
        String jsonSignUp = objectMapper.writeValueAsString(signUpDTO);
        mvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignUp))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MotionException));
    }
}
