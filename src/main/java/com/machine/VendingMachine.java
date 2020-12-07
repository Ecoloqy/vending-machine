package com.machine;

import com.machine.models.Coin;
import com.machine.models.Product;

import java.util.HashMap;
import java.util.Map;

public class VendingMachine {
    private final Map<Product, Integer> products;
    private final Map<Coin, Integer> coinReturn = new HashMap<>();

    public VendingMachine(Map<Product, Integer> products) {
        this.products = products;
    }

    public String getDisplay() {
        return "INSERT COIN";
    }

    public void insertCoin(Coin coin) {

    }

    public Map<Coin, Integer> getCoinReturn() {
        return coinReturn;
    }

    public Product selectProduct(Product product) {
        return product;
    }
}
