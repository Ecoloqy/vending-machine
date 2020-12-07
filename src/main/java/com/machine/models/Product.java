package com.machine.models;

import java.math.BigDecimal;

public enum Product {
    COLA ("1.00"),
    CHIPS ("0.50"),
    CANDY ("0.65");

    private final BigDecimal value;

    Product(String value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }
}
