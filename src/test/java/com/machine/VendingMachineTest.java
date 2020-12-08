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
    private Map<Coin, Integer> coins;
    private VendingMachine vendingMachine;

    @BeforeEach
    public void init() {
        products = new HashMap<>();
        products.put(Product.COLA, 5);
        products.put(Product.CANDY, 3);
        products.put(Product.CHIPS, 6);

        coins = new HashMap<>();
        coins.put(Coin.DIME, 8);
        coins.put(Coin.NICKEL, 3);
        coins.put(Coin.QUARTER, 12);

        vendingMachine = new VendingMachine(products, coins);
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
    public void when_customerSelectColaWhenInsertedValidPrice_expect_colaIsReturned() {
        Product expectedProduct = Product.COLA;
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        Product obtainedProduct = vendingMachine.selectProduct(Product.COLA);

        assertEquals(expectedProduct, obtainedProduct);
    }

    @Test
    public void when_customerSelectProductWhenInsertedNotEnoughMoney_expect_priceOfProductIsDisplayed() {
        String expectation = "CANDY - PRICE: $0.65, INSERTED: $0.50";

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        Product obtainedProduct = vendingMachine.selectProduct(Product.CANDY);
        String message = vendingMachine.getDisplay();

        assertEquals(expectation, message);
        assertNull(obtainedProduct);
    }

    @Test
    public void when_customerBuyProduct_expect_thankYouMessageShowsAndThenInsertCoinShows() {
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

    @Test
    public void when_customerSelectProductWhenInsertedToMuchMoney_expect_colaIsReturnedAndReturnIsObtained() {
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
    public void when_userProvideCoinsEqualsProductValue_expect_noCoinsInReturnSlot() {
        Map<Coin, Integer> expectedCoinReturn = new HashMap<>();

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.DIME);
        vendingMachine.insertCoin(Coin.NICKEL);

        vendingMachine.selectProduct(Product.CANDY);
        Map<Coin, Integer> obtainedCoinReturn = vendingMachine.getCoinReturn();

        assertEquals(expectedCoinReturn, obtainedCoinReturn);
    }

    @Test
    public void when_userSelectAbortButton_expect_messageOnDisplayShowsInsertCoin() {
        String expectation = "INSERT COIN";
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        vendingMachine.abortPurchase();
        String message = vendingMachine.getDisplay();

        assertEquals(expectation, message);
    }

    @Test
    public void when_userSelectAbortButton_expect_allInsertedMoneyInReturnSlot() {
        Map<Coin, Integer> expectedCoinReturn = new HashMap<>();
        expectedCoinReturn.put(Coin.QUARTER, 2);
        expectedCoinReturn.put(Coin.DIME, 1);

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.DIME);

        vendingMachine.abortPurchase();
        Map<Coin, Integer> obtainedCoinReturn = vendingMachine.getCoinReturn();

        assertEquals(expectedCoinReturn, obtainedCoinReturn);
    }

    @Test
    public void when_userSelectProductThatIsUnavailable_expect_machineShowsSoldOutMessageAndThenInsertCoin() {
        String expectation = "SOLD OUT";
        String expectation2 = "INSERT COIN";
        products.remove(Product.CHIPS);

        vendingMachine.selectProduct(Product.CHIPS);
        String message = vendingMachine.getDisplay();
        String message2 = vendingMachine.getDisplay();

        assertEquals(expectation, message);
        assertEquals(expectation2, message2);
    }

    @Test
    public void when_userSelectProductThatIsUnavailable_expect_machineShowsSoldOutMessageAndThenAmountOfInsertedMoney() {
        String expectation = "SOLD OUT";
        String expectation2 = "$0.50";
        products.remove(Product.CHIPS);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        vendingMachine.selectProduct(Product.CHIPS);
        String message = vendingMachine.getDisplay();
        String message2 = vendingMachine.getDisplay();

        assertEquals(expectation, message);
        assertEquals(expectation2, message2);
    }

    @Test
    public void when_tryingToGetReturnWhenVendingMachineIsBroke_expect_notAvailableChangeMessage() {
        String expectation = "EXACT CHANGE ONLY";
        coins.remove(Coin.QUARTER);
        coins.remove(Coin.NICKEL);
        coins.remove(Coin.DIME);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        vendingMachine.selectProduct(Product.CANDY);
        String message = vendingMachine.getDisplay();

        assertEquals(expectation, message);
    }

    @Test
    public void when_tryingToGetReturnWhenVendingMachineHaveMoney_expect_returnCoinsInInnerValuesThanInserted() {
        Map<Coin, Integer> expectedCoinReturn = new HashMap<>();
        expectedCoinReturn.put(Coin.DIME, 1);

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        vendingMachine.selectProduct(Product.CANDY);
        Map<Coin, Integer> obtainedCoinReturn = vendingMachine.getCoinReturn();

        assertEquals(expectedCoinReturn, obtainedCoinReturn);
    }

    @Test
    public void when_insertedMultiplyCoinsAndMachineDontHaveQuartersAndDimes_expect_returnOnlyInNickels() {
        Map<Coin, Integer> expectedCoinReturn = new HashMap<>();
        expectedCoinReturn.put(Coin.NICKEL, 2);
        coins.remove(Coin.QUARTER);
        coins.remove(Coin.DIME);

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        vendingMachine.selectProduct(Product.COLA);
        Map<Coin, Integer> obtainedCoinReturn = vendingMachine.getCoinReturn();

        assertEquals(expectedCoinReturn, obtainedCoinReturn);
    }
}