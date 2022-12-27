package com.saimon.motion.util;

import com.saimon.motion.DTOs.SignUpDTO;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.repository.UserRepository;

public class UtilTest {

    private static final String NAME = "testName";
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "teste";
    private static final String CPF = "12345678901";
    private static final String PHONE = "11222223333";
    private static final String PASSWORD_ENCODED = "$2a$10$y7BaCXMYDE6xkJl76bKedewKKlz2vd80dPQM9AFxS6OaBaqu2PK6S";

    public static SignUpDTO getSignUpDTO() {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setName(NAME);
        signUpDTO.setUsername(USERNAME);
        signUpDTO.setPassword(PASSWORD);
        signUpDTO.setCpf(CPF);
        signUpDTO.setPhone(PHONE);

        return signUpDTO;
    }

    public static MotionUser getMotionUser() {
        MotionUser motionUser = new MotionUser();
        motionUser.setUsername(USERNAME);
        motionUser.setName(NAME);
        motionUser.setPassword(PASSWORD_ENCODED);
        motionUser.setCpf(CPF);
        motionUser.setPhone(PHONE);
        return motionUser;
    }

    public static void saveMotionUser(UserRepository userRepository){
        userRepository.save(getMotionUser());
    }

    public static String loginBody(){
        return "{\"username\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}";
    }
}
