package com.machine.models;

public enum Display {
    MACHINE_EMPTY("INSERT COIN"),
    PURCHASE_DONE("THANK YOU"),
    PRODUCT_UNAVAILABLE("SOLD OUT"),
    EXACT_CHANGE_ONLY("EXACT CHANGE ONLY");

    private final String message;

    Display(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
