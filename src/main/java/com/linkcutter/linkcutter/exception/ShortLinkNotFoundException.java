package com.linkcutter.linkcutter.exception;

import lombok.Getter;

@Getter
public class ShortLinkNotFoundException extends RuntimeException {
    private final String message;

    public ShortLinkNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
