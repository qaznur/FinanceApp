package ru.javabegin.tutorial.androidfinance.core.interfaces;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import ru.javabegin.tutorial.androidfinance.core.exceptions.CurrencyException;
import ru.javabegin.tutorial.androidfinance.core.exceptions.AmountException;

public interface Storage extends TreeNode{

    // Получение баланса(остатка)
    BigDecimal getAmount(Currency currency) throws CurrencyException;
    BigDecimal getApproxAmount(Currency currency);
    Map<Currency, BigDecimal> getCurrencyAmounts();

    void updateAmount(BigDecimal amount, Currency currency) throws AmountException, CurrencyException;

    // Работа с валютой
    void addCurrency(Currency currency, BigDecimal initAmount) throws CurrencyException;
    void deleteCurrency(Currency currency) throws CurrencyException;
    Currency getCurrency(String code) throws CurrencyException;
    List<Currency> getAvailableCurrencies();
}
