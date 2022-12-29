package com.saimon.motion.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimon.motion.DTOs.SignInDTO;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.exception.MotionException;
import com.saimon.motion.repository.RoleRepository;
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
    RoleRepository roleRepository;

    @Test
    @DisplayName("Sign Up with User return Created")
    public void signUpWithUser() throws Exception {
        UtilTest utilTest = new UtilTest(userRepository, roleRepository);
        String newName = "newName";
        SignInDTO signInDTO = utilTest.getSignInDTO(newName);
        MotionUser motionUser = utilTest.createUser(newName);

        String jsonSignIn = objectMapper.writeValueAsString(signInDTO);
        String jsonMotionUser = objectMapper.writeValueAsString(motionUser.getUserRef());

        mvc.perform(MockMvcRequestBuilders.post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignIn))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("username").value(newName))
                .andExpect(MockMvcResultMatchers.content().string(jsonMotionUser));

    }

    @Test
    @DisplayName("User already exists return Exception")
    public void signUpAlreadyUser() throws Exception {
        UtilTest utilTest = new UtilTest(userRepository, roleRepository);
        SignInDTO signInDTO = utilTest.getSignInDTO();
        String jsonSignUp = objectMapper.writeValueAsString(signInDTO);
        mvc.perform(MockMvcRequestBuilders.post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignUp))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MotionException));
    }
}

