package com.machine;

import com.machine.models.Coin;
import com.machine.models.Display;
import com.machine.models.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class VendingMachine {
    private final Map<Product, Integer> products;
    private final Map<Coin, Integer> coinsInVendingMachine = new HashMap<>();
    private final Map<Coin, Integer> coinReturn = new HashMap<>();
    private String display;

    public VendingMachine(Map<Product, Integer> products) {
        this.products = products;
        this.display = Display.MACHINE_EMPTY.getMessage();
    }

    public String getDisplay() {
        return display;
    }

    public void insertCoin(Coin coin) {
        if (coin.equals(Coin.PENNY)) addCoinToContainer(coin, coinReturn);
        else addCoinToContainer(coin, coinsInVendingMachine);
    }

    public Map<Coin, Integer> getCoinReturn() {
        return coinReturn;
    }

    public Product selectProduct(Product product) {
        return product;
    }

    private void addCoinToContainer(Coin coin, Map<Coin, Integer> container) {
        if (container.containsKey(coin)) container.put(coin, container.get(coin) + 1);
        else container.put(coin, 1);
        updateMoneyOnDisplay();
    }

    private void updateMoneyOnDisplay() {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Map.Entry<Coin, Integer> entry : coinsInVendingMachine.entrySet()) {
            BigDecimal itemCost = entry.getKey().getValue().multiply(new BigDecimal(entry.getValue()));
            totalCost = totalCost.add(itemCost);
        }
        display = totalCost.equals(BigDecimal.ZERO) ? Display.MACHINE_EMPTY.getMessage() : "$" + totalCost;
    }
}
