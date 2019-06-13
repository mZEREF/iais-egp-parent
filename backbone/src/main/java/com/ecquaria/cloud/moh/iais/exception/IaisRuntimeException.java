package com.ecquaria.cloud.moh.iais.exception;

public class IaisRuntimeException extends RuntimeException {
    public IaisRuntimeException(Throwable cause) {
        super(cause);
    }

    public IaisRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IaisRuntimeException(String message) {
        super(message);
    }
}
