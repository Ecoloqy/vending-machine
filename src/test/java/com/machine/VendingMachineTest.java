package com.machine;

import com.machine.models.Coin;
import com.machine.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VendingMachineTest {
    private Map<Product, Integer> products;
    private VendingMachine vendingMachine;

    @BeforeEach
    public void init() {
        products = new HashMap<>();
        products.put(Product.COLA, 5);
        products.put(Product.CANDY, 3);
        products.put(Product.CHIPS, 6);
        vendingMachine = new VendingMachine(products);
    }

    @Test
    public void when_nothingInserted_expect_vendingMachineShowsInsertCoin() {
        String expectation = "INSERT COIN";

        String message = vendingMachine.getDisplay();

        assertEquals(expectation, message);
    }

    @Test
    public void when_insertNickel_expect_vendingMachineAcceptCoinAndShowResult() {
        String expectation = "$0.05";

        vendingMachine.insertCoin(Coin.NICKEL);
        String message = vendingMachine.getDisplay();

        assertEquals(expectation, message);
    }

    @Test
    public void when_addingMultipleCoins_expect_vendingMachineShowValidValueInCents() {
        String expectation = "$0.35";

        vendingMachine.insertCoin(Coin.DIME);
        vendingMachine.insertCoin(Coin.QUARTER);
        String message = vendingMachine.getDisplay();

        assertEquals(expectation, message);
    }

    @Test
    public void when_addingMultipleCoinsOfValue1Dollar_expect_vendingMachineShowValidValueInDollars() {
        String expectation = "$1.00";

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        String message = vendingMachine.getDisplay();

        assertEquals(expectation, message);
    }

    @Test
    public void when_addingMultipleCoinsOfValue2DollarsAndMore_expect_vendingMachineShowValidValueInDollars() {
        String expectation = "$2.35";

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.DIME);
        vendingMachine.insertCoin(Coin.QUARTER);
        String message = vendingMachine.getDisplay();

        assertEquals(expectation, message);
    }

    @Test
    public void when_insertPennie_expect_vendingMachineRejectCoin() {
        String expectation = "INSERT COIN";

        vendingMachine.insertCoin(Coin.PENNY);
        String message = vendingMachine.getDisplay();

        assertEquals(expectation, message);
    }

    @Test
    public void when_insertPennie_expect_vendingMachinePlacePennieIntoCoinReturn() {
        Map<Coin, Integer> expectedCoinReturn = new HashMap<>();
        expectedCoinReturn.put(Coin.PENNY, 1);

        vendingMachine.insertCoin(Coin.PENNY);
        Map<Coin, Integer> obtainedCoinReturn = vendingMachine.getCoinReturn();

        assertEquals(expectedCoinReturn, obtainedCoinReturn);
    }

    @Test
    public void when_customerSelectColaWhenInsertedValidPrice_colaIsReturned() {
        Product expectedProduct = Product.COLA;
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        Product obtainedProduct = vendingMachine.selectProduct(Product.COLA);

        assertEquals(expectedProduct, obtainedProduct);
    }

    @Test
    public void when_customerSelectProductWhenInsertedToMuchMoney_colaIsReturnedAndReturnIsObtained() {
        Map<Coin, Integer> expectedCoinReturn = new HashMap<>();
        expectedCoinReturn.put(Coin.QUARTER, 1);

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        vendingMachine.selectProduct(Product.CHIPS);
        Map<Coin, Integer> obtainedCoinReturn = vendingMachine.getCoinReturn();

        assertEquals(expectedCoinReturn, obtainedCoinReturn);
    }

    @Test
    public void when_customerSelectProductWhenInsertedNotEnoughMoney_priceOfProductIsDisplayed() {
        String expectation = "CANDY - PRICE: $0.65, INSERTED: $0.50";

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        Product obtainedProduct = vendingMachine.selectProduct(Product.CANDY);
        String message = vendingMachine.getDisplay();

        assertEquals(expectation, message);
        assertNull(obtainedProduct);
    }

    @Test
    public void when_customerBuyProduct_thankYouMessageShowsAndThenInsertCoinShows() {
        String expectation = "THANK YOU";
        String expectation2 = "INSERT COIN";

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        vendingMachine.selectProduct(Product.CHIPS);
        String message = vendingMachine.getDisplay();
        String message2 = vendingMachine.getDisplay();

        assertEquals(expectation, message);
        assertEquals(expectation2, message2);
    }
}