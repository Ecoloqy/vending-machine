package com.machine;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineTest {

    private static VendingMachine vendingMachine;

    @BeforeAll
    public static void onStart() {
        vendingMachine = new VendingMachine();
    }

    @Test
    public void when_a_should_b() {

    }
}