package ru.javabegin.tutorial.androidfinance.core.dao.interfaces;

import java.util.List;

public interface CommonDAO<T> {

    List<T> getAll();
    T get(long id);
    boolean update(T object);
    boolean delete(T object);
    boolean add(T object);

}
