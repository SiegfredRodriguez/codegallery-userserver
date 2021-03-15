package com.siegfred.userserver.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(String message) {
        super(message);
    }
}
