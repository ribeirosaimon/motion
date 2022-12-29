package com.saimon.motion.api.controller;

import com.saimon.motion.DTOs.SignInDTO;
import com.saimon.motion.api.service.MotionUserService;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.security.MotionLoggedUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MotionUserController {

    private final MotionUserService motionUserService;

    @PostMapping("/signin")
    public MotionUser.MotionUserRef saveMotionUser(@RequestBody SignInDTO newUserDTO) throws Exception {
        return motionUserService.signUpUser(newUserDTO);
    }

    @PostMapping({"/inactive/{id}", "/inactive"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity inactiveMotionUser(MotionLoggedUser motionLoggedUser,
                                             @PathVariable(value = "id", required = false) Long id) {
        motionUserService.inactiveUser(motionLoggedUser.getMotionUser().getId(), id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/promotion/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public MotionUser.MotionUserRef promotionToAdmin(@PathVariable Long id){
        return motionUserService.promoteToAdmin(id);
    }
}
