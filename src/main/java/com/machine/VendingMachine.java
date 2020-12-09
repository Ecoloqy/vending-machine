package com.machine;

import com.machine.models.Coin;
import com.machine.models.Display;
import com.machine.models.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class VendingMachine {
    private final Map<Product, Integer> products;
    private Map<Coin, Integer> coinsInVendingMachine;
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

    /**
     * @return product if all conditions are met, but null when something goes wrong like no money in vending machine,
     *      invalid amount of money in machine slot or vending machine can't return exchange
     */
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
        if (!giveReturnToUser(product)) {
            display = Display.EXACT_CHANGE_ONLY.getMessage();
            return null;
        }
        removeProductFromMachine(product);
        clearMachineStatus();
        display = Display.PURCHASE_DONE.getMessage();
        return product;
    }

    public void abortPurchase() {
        sendAllCoinsFromOneContainerToAnother(coinsInBuyInMode, coinsReturn);
        coinsInBuyInMode.clear();
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
     * <p>And that algorithm works that way...</p>
     * <p>Method giveReturnToUser() on start checks if it possible to return exchange, in the same time calculates this exchange to minimize loop circuits.</p>
     * <p>Coins are sorted by coin value from max to min, because if we can return $0.25, we can return 1 QUARTER instead of 2 DIMES and 1 NICKEL, only when
     * QUARTER is unavailable rest is checked</p>
     * <p>- On the end if sumOfCoins to return is not equal 0 then there is no variant to return exchange, method returns false and stop further operations.</p>
     * <p>- If sumOfCoins is equal 0 then there is a way to return exchange and this way is already done and saved as coinsToReturn HashMap.</p>
     * <p>Next to it is running giveUserReturnAndUpdateEarningsInVendingMachine() method where I change maps each other, like:</p>
     * <p>- all rest coins in coinsInBuyInMode (pocket where user put coins) are added to coinsInVendingMachine (set of starting machine coins)</p>
     * <p>- coinsInBuyInMode are cleared because account is now $0.00</p>
     * <p>- and at the end this coinsToReturn (map of coins that are possible to return) are send to return pocket of vending machine, when user
     * can grab them and carry out further purchases</p>
     */
    private boolean giveReturnToUser(Product product) {
        Map<Coin, Integer> coinsToReturn = new HashMap<>();

        BigDecimal sumOfCoins = getSumOfCoinsInBuyInMode().subtract(product.getValue());
        List<Coin> validCoinsToReturn = Arrays.stream(Coin.values()).sorted(Comparator.comparing(Coin::getValue).reversed())
                .filter(e -> !e.equals(Coin.PENNY))
                .collect(Collectors.toList());

        for (Coin coin : validCoinsToReturn) {
            if (!coinInContainer(coin, coinsInBuyInMode) && !coinInContainer(coin, coinsInVendingMachine)) continue;
            if (sumOfCoins.doubleValue() > 0)
                sumOfCoins = updateCoinsByRest(coinsToReturn, sumOfCoins, coin);
        }

        if (sumOfCoins.compareTo(BigDecimal.ZERO) != 0) return false;

        giveUserReturnAndUpdateEarningsInVendingMachine(coinsToReturn);
        return true;
    }

    private BigDecimal updateCoinsByRest(Map<Coin, Integer> coinsToReturn, BigDecimal value, Coin coin) {
        BigDecimal coinAmount = value.divide(coin.getValue(), RoundingMode.FLOOR);
        BigDecimal coinRest = value.remainder(coin.getValue());

        List<Coin> coinsToAdd = new ArrayList<>();
        int counter = coinAmount.intValue();
        for (int i = 0; i < coinAmount.intValue(); i++) {
            counter = updateCounterAndSwapPlacesIfCoinExistInVendingMachine(coinsToAdd, coin, counter);
        }

        addMultipleCoinsToContainer(coinsToAdd, coinsToReturn);
        return new BigDecimal(counter).add(coinRest);
    }

    private int updateCounterAndSwapPlacesIfCoinExistInVendingMachine(List<Coin> coinsToAdd, Coin coin, int counter) {
        if (coinInContainer(coin, coinsInBuyInMode)) {
            removeCoinFromContainer(coin, coinsInBuyInMode);
            coinsToAdd.add(coin);
            counter--;
        } else if (coinInContainer(coin, coinsInVendingMachine)) {
            removeCoinFromContainer(coin, coinsInVendingMachine);
            coinsToAdd.add(coin);
            counter--;
        }
        return counter;
    }

    private void giveUserReturnAndUpdateEarningsInVendingMachine(Map<Coin, Integer> coinsToReturn) {
        sendAllCoinsFromOneContainerToAnother(coinsInBuyInMode, coinsInVendingMachine);
        clearMachineStatus();
        sendAllCoinsFromOneContainerToAnother(coinsToReturn, coinsReturn);
    }

    private boolean coinInContainer(Coin coin, Map<Coin, Integer> container) {
        return container.containsKey(coin) && container.get(coin) > 0;
    }

    private void removeCoinFromContainer(Coin coin, Map<Coin, Integer> container) {
        if (container.get(coin) == 1) container.remove(coin);
        else container.put(coin, container.get(coin) - 1);
        updateMoneyOnDisplay();
    }

    private void addMultipleCoinsToContainer(List<Coin> coins, Map<Coin, Integer> container) {
        for (Coin coin : coins) {
            addCoinToContainer(coin, container);
        }
    }

    private void addCoinToContainer(Coin coin, Map<Coin, Integer> container) {
        if (container.containsKey(coin)) container.put(coin, container.get(coin) + 1);
        else container.put(coin, 1);
        updateMoneyOnDisplay();
    }

    private void clearMachineStatus() {
        coinsInBuyInMode.clear();
    }

    private String calculateNeededSumOfCoinsAndSetUpMessage(Product product) {
        String productName = product.name();
        BigDecimal valueOfProduct = product.getValue().setScale(2, RoundingMode.CEILING);
        BigDecimal insertedCoinsValue = getSumOfCoinsInBuyInMode();
        return productName + " - PRICE: $" + valueOfProduct + ", INSERTED: $" + insertedCoinsValue;
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

    private void sendAllCoinsFromOneContainerToAnother(Map<Coin, Integer> container1, Map<Coin, Integer> container2) {
        for (Map.Entry<Coin, Integer> entry : container1.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                addCoinToContainer(entry.getKey(), container2);
            }
        }
    }
}
