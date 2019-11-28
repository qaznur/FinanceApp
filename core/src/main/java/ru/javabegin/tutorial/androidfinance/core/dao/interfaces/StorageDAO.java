package ru.javabegin.tutorial.androidfinance.core.dao.interfaces;

import java.math.BigDecimal;
import java.util.Currency;

import ru.javabegin.tutorial.androidfinance.core.exceptions.CurrencyException;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Storage;

public interface StorageDAO extends CommonDAO<Storage>{

    boolean addCurrency(Storage storage, Currency currency, BigDecimal amount) throws CurrencyException;
    boolean deleteCurrency(Storage storage, Currency currency) throws CurrencyException;
    boolean updateAmount(Storage storage, Currency currency, BigDecimal amount);

}
