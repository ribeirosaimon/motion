package com.saimon.motion.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimon.motion.DTOs.SignInDTO;
import com.saimon.motion.api.controller.MotionUserController;
import com.saimon.motion.api.service.MotionUserService;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.exception.MotionException;
import com.saimon.motion.repository.UserRepository;
import com.saimon.motion.util.UtilTest;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(MotionUserController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @MockBean
    MotionUserService motionUserService;

    @BeforeAll
    public void setUp() throws Exception {
//        UtilTest.saveMotionUser(userRepository);
        Mockito.when(motionUserService.signUpUser(ArgumentMatchers.any(SignInDTO.class))).thenReturn(UtilTest.getMotionUser().getUserRef());
    }

    @Test
    @DisplayName("Sign Up with User return Created")
    public void signUpWithUser() throws Exception {
        SignInDTO signInDTO = UtilTest.getSignUpDTO();
        MotionUser motionUser = UtilTest.getMotionUser();

        String jsonSignIn = objectMapper.writeValueAsString(signInDTO);
        String jsonMotionUser = objectMapper.writeValueAsString(motionUser.getUserRef());

        mvc.perform(MockMvcRequestBuilders.post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignIn))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("testName"))
                .andExpect(MockMvcResultMatchers.content().string(jsonMotionUser));

    }

    @Test
    @DisplayName("User already exists return Exception")
    public void signUpAlreadyUser() throws Exception {
        SignInDTO signInDTO = UtilTest.getSignUpDTO();
        String jsonSignUp = objectMapper.writeValueAsString(signInDTO);
        mvc.perform(MockMvcRequestBuilders.post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignUp))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MotionException));
    }
}
