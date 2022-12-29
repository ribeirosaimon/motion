package com.saimon.motion.serviceTest;

import com.saimon.motion.api.service.MotionUserService;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.repository.RoleRepository;
import com.saimon.motion.repository.UserRepository;
import com.saimon.motion.util.UtilTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTest {

    @Autowired
    private MotionUserService motionUserService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    UtilTest utilTest;

    @BeforeAll
    public void setUp() throws Exception {
        utilTest = new UtilTest(userRepository, roleRepository);
        utilTest.saveMotionUser();
    }

    @Test
    @DisplayName("have to save one user")
    public void haveToSaveOneUser() throws Exception {
        String newUsername = "newUsername";
        MotionUser.MotionUserRef motionUserRef = motionUserService.signUpUser(this.utilTest.getSignInDTO(newUsername));
        MotionUser foundUser = userRepository.findByUsername(motionUserRef.getUsername()).get();
        Assertions.assertEquals(foundUser.getCpf(), motionUserRef.getCpf());
        Assertions.assertEquals(foundUser.getUsername(), newUsername);
    }
}
