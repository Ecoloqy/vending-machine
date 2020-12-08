package com.machine;

import com.machine.models.Coin;
import com.machine.models.Display;
import com.machine.models.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class VendingMachine {
    private final Map<Product, Integer> products;
    private final Map<Coin, Integer> coinsInVendingMachine;
    private final Map<Coin, Integer> coinsInBuyInMode = new HashMap<>();
    private final Map<Coin, Integer> coinsReturn = new HashMap<>();
    private String display;
    private boolean isPurchaseInProgress = false;

    public VendingMachine(Map<Product, Integer> products, Map<Coin, Integer> coinsInVendingMachine) {
        this.products = products;
        this.coinsInVendingMachine = coinsInVendingMachine;
        this.display = Display.MACHINE_EMPTY.getMessage();
    }

    public String getDisplay() {
        if (isPurchaseInProgress) isPurchaseInProgress = false;
        return display;
    }

    public void insertCoin(Coin coin) {
        if (coin.equals(Coin.PENNY)) addCoinToContainer(coin, coinsReturn);
        else addCoinToContainer(coin, coinsInBuyInMode);
    }

    public Map<Coin, Integer> getCoinReturn() {
        return coinsReturn;
    }

    public Product selectProduct(Product product) {
        isPurchaseInProgress = true;
        if (purchaseCanBeDone()) {
            doPurchaseOfProduct();
            display = Display.PURCHASE_DONE.getMessage();
            return product;
        }
        display = calculateNeededSumOfCoinsAndSetUpMessage(product);
        return null;
    }

    private boolean purchaseCanBeDone() {
        return true;
    }

    private void doPurchaseOfProduct() {

    }

    private String calculateNeededSumOfCoinsAndSetUpMessage(Product product) {
        return "";
    }

    private void addCoinToContainer(Coin coin, Map<Coin, Integer> container) {
        if (container.containsKey(coin)) container.put(coin, container.get(coin) + 1);
        else container.put(coin, 1);
        updateMoneyOnDisplay();
    }

    private void updateMoneyOnDisplay() {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Map.Entry<Coin, Integer> entry : coinsInBuyInMode.entrySet()) {
            BigDecimal itemCost = entry.getKey().getValue().multiply(new BigDecimal(entry.getValue()));
            totalCost = totalCost.add(itemCost);
        }
        display = totalCost.equals(BigDecimal.ZERO) ? Display.MACHINE_EMPTY.getMessage() : "$" + totalCost;
    }
}
