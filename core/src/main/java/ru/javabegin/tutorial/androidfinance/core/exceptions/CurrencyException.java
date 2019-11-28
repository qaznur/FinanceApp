package ru.javabegin.tutorial.androidfinance.core.exceptions;

public class CurrencyException extends Exception {

    public CurrencyException() {
    }

    public CurrencyException(String s) {
        super(s);
    }

    public CurrencyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CurrencyException(Throwable throwable) {
        super(throwable);
    }

    public CurrencyException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
