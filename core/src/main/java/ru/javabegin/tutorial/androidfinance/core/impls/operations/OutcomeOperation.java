package ru.javabegin.tutorial.androidfinance.core.impls.operations;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;

import ru.javabegin.tutorial.androidfinance.core.abstracts.AbstractOperation;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Storage;

public class OutcomeOperation extends AbstractOperation {

    private Storage fromStorage;
    private Source toSource;
    private Currency fromCurrency;
    private BigDecimal fromAmount;

    public OutcomeOperation() {
    }

    public OutcomeOperation(long id, Calendar dateTime, String addInfo, Storage fromStorage, Source toSource, Currency fromCurrency, BigDecimal fromAmount) {
        super(id, dateTime, addInfo);
        this.fromStorage = fromStorage;
        this.toSource = toSource;
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
    }

    public OutcomeOperation(long id, Storage fromStorage, Source toSource, Currency fromCurrency, BigDecimal fromAmount) {
        super(id);
        this.fromStorage = fromStorage;
        this.toSource = toSource;
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
    }

    public OutcomeOperation(Storage fromStorage, Source toSource, Currency fromCurrency, BigDecimal fromAmount) {
        this.fromStorage = fromStorage;
        this.toSource = toSource;
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
    }

    public Storage getFromStorage() {
        return fromStorage;
    }

    public void setFromStorage(Storage fromStorage) {
        this.fromStorage = fromStorage;
    }

    public Source getToSource() {
        return toSource;
    }

    public void setToSource(Source toSource) {
        this.toSource = toSource;
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
