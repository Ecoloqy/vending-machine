package com.machine.models;

public enum Display {
    MACHINE_EMPTY("INSERT COIN"),
    PURCHASE_DONE("THANK YOU");

    private final String message;

    Display(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
