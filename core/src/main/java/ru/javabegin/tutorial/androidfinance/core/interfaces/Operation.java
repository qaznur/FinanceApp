package ru.javabegin.tutorial.androidfinance.core.interfaces;

import java.util.Calendar;

import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public interface Operation extends Comparable<Operation> {

    long getId();

    void setId(long id);

    OperationType getOperationType();

    Calendar getDateTime();

    String getDescription();

}
