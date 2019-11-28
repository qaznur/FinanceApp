package ru.javabegin.tutorial.androidfinance.core.impls;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.javabegin.tutorial.androidfinance.core.abstracts.AbstractTreeNode;
import ru.javabegin.tutorial.androidfinance.core.exceptions.AmountException;
import ru.javabegin.tutorial.androidfinance.core.exceptions.CurrencyException;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Storage;

public class DefaultStorage extends AbstractTreeNode implements Storage {

    private Map<Currency, BigDecimal> currencyAmounts = new HashMap<>();
    private List<Currency> currencyList = new ArrayList<>();


    public DefaultStorage() {
    }

    public DefaultStorage(String name) {
        super(name);
    }

    public DefaultStorage(long id, String name) {
        super(id, name);
    }

    public DefaultStorage(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    public DefaultStorage(Map<Currency, BigDecimal> currencyAmounts) {
        this.currencyAmounts = currencyAmounts;
    }

    public DefaultStorage(String name, Map<Currency, BigDecimal> currencyAmounts, List<Currency> currencyList) {
        super(name);
        this.currencyAmounts = currencyAmounts;
        this.currencyList = currencyList;
    }

    private void checkCurrencyExist(Currency currency) throws CurrencyException {
        if(!currencyAmounts.containsKey(currency)) {
            throw new CurrencyException("Currency " + currency + " doesn't exist");
        }
    }

    private void checkAmount(BigDecimal amount) throws AmountException {
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountException("Amount can't be < 0");
        }
    }

    @Override
    public void addCurrency(Currency currency, BigDecimal initAmount) throws CurrencyException {
        if(currencyAmounts.containsKey(currency)) {
            throw new CurrencyException("Currency already exist");
        }

        currencyList.add(currency);
        currencyAmounts.put(currency, initAmount);
    }

    @Override
    public void deleteCurrency(Currency currency) throws CurrencyException {
        checkCurrencyExist(currency);

//        if(!currencyAmounts.get(currency).equals(BigDecimal.ZERO)) {
//            throw new CurrencyException("Can't delete currency with amount");
//        }

        currencyList.remove(currency);
        currencyAmounts.remove(currency);
    }

    @Override
    public Currency getCurrency(String code) throws CurrencyException {
        for (Currency currency : currencyList) {
            if(currency.getCurrencyCode().equals(code)) {
                return currency;
            }
        }
        throw new CurrencyException("Currency (code=" + code + ") doesn't exist in storage");
    }

    @Override
    public void updateAmount(BigDecimal amount, Currency currency) throws CurrencyException, AmountException {
        checkCurrencyExist(currency);
        checkAmount(amount);
        currencyAmounts.put(currency, amount);
    }

    @Override
    public BigDecimal getApproxAmount(Currency currency) {
        // TODO implement later
        return null;
    }

    @Override
    public BigDecimal getAmount(Currency currency) throws CurrencyException {
        checkCurrencyExist(currency);
        return currencyAmounts.get(currency);
    }

    public List<Currency> getAvailableCurrencies() {
        return currencyList;
    }

    @Override
    public Map<Currency, BigDecimal> getCurrencyAmounts() {
        return currencyAmounts;
    }

}
