package ru.javabegin.tutorial.androidfinance.core.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface CommonDAO<T> {

    List<T> getAll();
    T get(long id);
    boolean update(T object) throws SQLException;
    boolean delete(T object) throws SQLException;
    boolean add(T object) throws SQLException;

}
