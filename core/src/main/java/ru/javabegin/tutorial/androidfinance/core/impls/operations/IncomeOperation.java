package ru.javabegin.tutorial.androidfinance.core.impls.operations;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;

import ru.javabegin.tutorial.androidfinance.core.abstracts.AbstractOperation;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Storage;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public class IncomeOperation extends AbstractOperation {

    private Source fromSource;
    private Storage toStorage;
    private Currency fromCurrency;
    private BigDecimal fromAmount;

    public IncomeOperation() {
        setOperationType(OperationType.INCOME);
    }

    public IncomeOperation(long id, Calendar dateTime, String description, Source fromSource, Storage toStorage, Currency fromCurrency, BigDecimal fromAmount) {
        super(id, dateTime, description);
        this.fromSource = fromSource;
        this.toStorage = toStorage;
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
    }

    public IncomeOperation(long id, Source fromSource, Storage toStorage, Currency fromCurrency, BigDecimal fromAmount) {
        super(id);
        this.fromSource = fromSource;
        this.toStorage = toStorage;
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
    }

    public IncomeOperation(Source fromSource, Storage toStorage, Currency fromCurrency, BigDecimal fromAmount) {
        this.fromSource = fromSource;
        this.toStorage = toStorage;
        this.fromCurrency = fromCurrency;
        this.fromAmount = fromAmount;
    }

    public Source getFromSource() {
        return fromSource;
    }

    public void setFromSource(Source fromSource) {
        this.fromSource = fromSource;
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
