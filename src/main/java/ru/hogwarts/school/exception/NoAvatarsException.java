package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "no avatars provided")
public class NoAvatarsException extends RuntimeException{
    public NoAvatarsException() {
    }
    public NoAvatarsException(String message) {
        super(message);
    }
}
