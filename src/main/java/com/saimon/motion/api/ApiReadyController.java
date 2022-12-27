package com.saimon.motion.api;

import com.saimon.motion.exception.FileNotFound;
import com.saimon.motion.exception.MotionException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@AllArgsConstructor
public class ApiReadyController {

    @GetMapping("/ready")
    public ReadyDTO readyApiController(HttpServletRequest request){
        String ipAddress = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For") : request.getRemoteAddr();
        return new ReadyDTO(ipAddress, new Date());
    }



    @Getter
    @Setter
    @AllArgsConstructor
    private static class ReadyDTO{
        private String ip;

        private Date readyIn;

    }
}
