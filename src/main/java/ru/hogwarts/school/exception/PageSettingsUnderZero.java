package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "page number or page size cant be equal or less than zero")
public class PageSettingsUnderZero extends RuntimeException {

    public PageSettingsUnderZero() {
    }

    public PageSettingsUnderZero(String message) {
        super(message);
    }
}
