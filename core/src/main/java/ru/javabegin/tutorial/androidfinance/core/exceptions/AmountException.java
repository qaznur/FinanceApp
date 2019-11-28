package ru.javabegin.tutorial.androidfinance.core.exceptions;

public class AmountException extends Exception {

    public AmountException() {
    }

    public AmountException(String s) {
        super(s);
    }

    public AmountException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AmountException(Throwable throwable) {
        super(throwable);
    }

    public AmountException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
