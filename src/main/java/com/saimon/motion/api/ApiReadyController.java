package com.saimon.motion.api;

import com.saimon.motion.security.MotionLoggedUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class ApiReadyController {

    @GetMapping("/ready")
    public ReadyDTO readyApiController(HttpServletRequest request, MotionLoggedUser motionUser) {
        String ipAddress = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For") : request.getRemoteAddr();
        return new ReadyDTO(ipAddress, new Date(), motionUser.getName(), motionUser.getMotionUser().getRoles());
    }


    @Getter
    @Setter
    @AllArgsConstructor
    private static class ReadyDTO {
        private String ip;

        private Date readyIn;

        private String myName;

        private List myRoles;

    }
}
