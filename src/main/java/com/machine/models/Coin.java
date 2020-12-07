package com.machine.models;

import java.math.BigDecimal;

public enum Coin {
    NICKEL("0.05"),
    DIME("0.10"),
    QUARTER("0.25"),
    PENNY("0.01");

    private final BigDecimal value;

    Coin(String value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }
}
