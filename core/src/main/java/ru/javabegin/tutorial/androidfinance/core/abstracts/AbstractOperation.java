package ru.javabegin.tutorial.androidfinance.core.abstracts;

import java.util.Calendar;

import ru.javabegin.tutorial.androidfinance.core.interfaces.Operation;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public abstract class AbstractOperation implements Operation {

    private long id;
    private Calendar dateTime;
    private String description;
    private OperationType operationType;

    public AbstractOperation(long id, Calendar dateTime, String description, OperationType operationType) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.operationType = operationType;
    }

    public AbstractOperation(long id, Calendar dateTime, String description) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
    }

    public AbstractOperation(long id, OperationType operationType) {
        this.id = id;
        this.operationType = operationType;
    }

    public AbstractOperation(long id) {
        this.id = id;
    }

    public AbstractOperation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
