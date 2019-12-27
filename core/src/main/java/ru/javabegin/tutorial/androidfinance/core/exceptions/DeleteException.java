package ru.javabegin.tutorial.androidfinance.core.exceptions;

public class DeleteException extends Exception {

    public DeleteException() {
    }

    public DeleteException(String s) {
        super(s);
    }

    public DeleteException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DeleteException(Throwable throwable) {
        super(throwable);
    }

    public DeleteException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
