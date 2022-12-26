package com.saimon.motion.controller;

import com.saimon.motion.security.MotionUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    @GetMapping("/teste")
    public ResponseEntity teste(MotionUser principal) {
        System.out.println(principal);
        return ResponseEntity.ok("DEU");
    }

}
