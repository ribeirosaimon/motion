package com.saimon.motion.DTOs;

import com.saimon.motion.domain.MotionUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInDTO {

    private String name;
    private String username;
    private String password;
    private MotionUser.Gender gender;
    private String phone;
}
