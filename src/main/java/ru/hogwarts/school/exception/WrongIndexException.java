package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Wrong index")
public class WrongIndexException extends RuntimeException{
    public WrongIndexException() {
    }
    public WrongIndexException(String message) {
        super(message);
    }
}
