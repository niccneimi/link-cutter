package com.linkcutter.linkcutter.exception;

import lombok.Getter;

@Getter
public class InsufficientSystemStateException extends RuntimeException {
    private final String message;

    public InsufficientSystemStateException(String message) {
        super(message);
        this.message = message;
    }
}
