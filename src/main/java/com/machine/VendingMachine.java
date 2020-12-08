package com.machine;

import com.machine.models.Coin;
import com.machine.models.Display;
import com.machine.models.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class VendingMachine {
    private final Map<Product, Integer> products;
    private final Map<Coin, Integer> coinsInVendingMachine;
    private final Map<Coin, Integer> coinsInBuyInMode = new HashMap<>();
    private final Map<Coin, Integer> coinsReturn = new HashMap<>();
    private String display;
    private boolean isInPurchaseMode = false;

    public VendingMachine(Map<Product, Integer> products, Map<Coin, Integer> coinsInVendingMachine) {
        this.products = products;
        this.coinsInVendingMachine = coinsInVendingMachine;
        this.display = Display.MACHINE_EMPTY.getMessage();
    }

    public String getDisplay() {
        if (isInPurchaseMode) {
            String toShow = display;
            updateMoneyOnDisplay();
            return toShow;
        }
        updateMoneyOnDisplay();
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
        isInPurchaseMode = true;
        if (!productInVendingMachine(product)) {
            display = Display.PRODUCT_UNAVAILABLE.getMessage();
            return null;
        }
        if (!insertedEqualOrMoreMoney(product)) {
            display = calculateNeededSumOfCoinsAndSetUpMessage(product);
            return null;
        }
        if (!vendingMachineCanReturnCoins(product)) {
            display = Display.EXACT_CHANGE_ONLY.getMessage();
            return null;
        }
        removeProductFromMachine(product);
        giveReturnToUser(product);
        clearMachineStatus();
        display = Display.PURCHASE_DONE.getMessage();
        return product;
    }

    public void abortPurchase() {
        sendAllCoinsFromOneContainerToAnother(coinsInBuyInMode, coinsReturn);
        coinsInBuyInMode.clear();
    }

    private void sendAllCoinsFromOneContainerToAnother(Map<Coin, Integer> container1, Map<Coin, Integer> container2) {
        for (Map.Entry<Coin, Integer> entry : container1.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                addCoinToContainer(entry.getKey(), container2);
            }
        }
    }

    private boolean productInVendingMachine(Product product) {
        return products.containsKey(product) && products.get(product) > 0;
    }

    private boolean insertedEqualOrMoreMoney(Product product) {
        return getSumOfCoinsInBuyInMode().doubleValue() >= product.getValue().doubleValue();
    }

    private void removeProductFromMachine(Product product) {
        if (products.get(product) == 1) products.remove(product);
        else products.put(product, products.get(product) - 1);
    }

    /**
     * Algorithm to return rest when in vending machine is available coins
     */

    private boolean vendingMachineCanReturnCoins(Product product) {
        return true;
    }

    private void giveReturnToUser(Product product) {

    }

    /**
     * End of algorithm to return rest when in vending machine is available coins
     */

    private void clearMachineStatus() {
        coinsInBuyInMode.clear();
    }

    private String calculateNeededSumOfCoinsAndSetUpMessage(Product product) {
        String productName = product.name();
        BigDecimal valueOfProduct = product.getValue().setScale(2, RoundingMode.CEILING);
        BigDecimal insertedCoinsValue = getSumOfCoinsInBuyInMode();
        return productName + " - PRICE: $" + valueOfProduct + ", INSERTED: $" + insertedCoinsValue;
    }

    private void addCoinToContainer(Coin coin, Map<Coin, Integer> container) {
        if (container.containsKey(coin)) container.put(coin, container.get(coin) + 1);
        else container.put(coin, 1);
        updateMoneyOnDisplay();
    }

    private void updateMoneyOnDisplay() {
        BigDecimal total = getSumOfCoinsInBuyInMode();
        display = total.compareTo(BigDecimal.ZERO) == 0 ? Display.MACHINE_EMPTY.getMessage() : "$" + total;
    }

    private BigDecimal getSumOfCoinsInBuyInMode() {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Coin, Integer> entry : coinsInBuyInMode.entrySet()) {
            BigDecimal itemCost = entry.getKey().getValue().multiply(new BigDecimal(entry.getValue()));
            total = total.add(itemCost);
        }

        return total.setScale(2, RoundingMode.CEILING);
    }
}
