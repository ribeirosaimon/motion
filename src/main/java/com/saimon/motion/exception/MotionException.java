package com.saimon.motion.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MotionException extends RuntimeException {

    private String message;

    public MotionException(String message) {
        super();
        this.message = message;
    }
}