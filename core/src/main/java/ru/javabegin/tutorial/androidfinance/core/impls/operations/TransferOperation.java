package ru.javabegin.tutorial.androidfinance.core.impls.operations;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;

import ru.javabegin.tutorial.androidfinance.core.abstracts.AbstractOperation;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Storage;

public class TransferOperation extends AbstractOperation {

    private Storage fromStorage;
    private Storage toStorage;
    private Currency fromCurrency;
    private BigDecimal fromAmount;


    public TransferOperation() {
    }

    public TransferOperation(long id, Calendar dateTime, String addInfo, Storage fromStorage, Storage toStorage, Currency fromCurrency, BigDecimal fromAmount) {
        super(id, dateTime, addInfo);
        this.fromStorage = fromStorage;
        this.toStorage = toStorage;
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
    }

    public TransferOperation(long id, Storage fromStorage, Storage toStorage, Currency fromCurrency, BigDecimal fromAmount) {
        super(id);
        this.fromStorage = fromStorage;
        this.toStorage = toStorage;
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
    }

    public TransferOperation(Storage fromStorage, Storage toStorage, Currency fromCurrency, BigDecimal fromAmount) {
        this.fromStorage = fromStorage;
        this.toStorage = toStorage;
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
    }

    public Storage getFromStorage() {
        return fromStorage;
    }

    public void setFromStorage(Storage fromStorage) {
        this.fromStorage = fromStorage;
    }

    public Storage getToStorage() {
        return toStorage;
    }

    public void setToStorage(Storage toStorage) {
        this.toStorage = toStorage;
    }

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(Currency fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }
}
