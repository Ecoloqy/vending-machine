package com.machine.handlers;

public class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Exception e) {
        super(message, e);
    }
}

