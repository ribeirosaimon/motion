package com.saimon.motion.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileNotFound extends RuntimeException{
    public FileNotFound() {
        super();
    }
}
