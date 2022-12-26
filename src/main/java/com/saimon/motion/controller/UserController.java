package com.saimon.motion.controller;

import com.saimon.motion.DTOs.LoginDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class UserController {

    @GetMapping("/teste")
    public ResponseEntity teste(Principal principal) {
        System.out.println(principal);
        return ResponseEntity.ok("DEU");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {

        return ResponseEntity.badRequest().body("ERRO");
    }
}
