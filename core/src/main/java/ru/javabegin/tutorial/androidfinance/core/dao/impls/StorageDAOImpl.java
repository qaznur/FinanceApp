package ru.javabegin.tutorial.androidfinance.core.dao.impls;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import ru.javabegin.tutorial.androidfinance.core.dao.interfaces.StorageDAO;
import ru.javabegin.tutorial.androidfinance.core.database.SQLiteConnection;
import ru.javabegin.tutorial.androidfinance.core.exceptions.CurrencyException;
import ru.javabegin.tutorial.androidfinance.core.impls.DefaultStorage;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Storage;

public class StorageDAOImpl implements StorageDAO {

    private static final String CURRENCY_TABLE = "currency_amount";
    private static final String STORAGE_TABLE = "storage";

    private List<Storage> storageList = new ArrayList<>();

    @Override
    public boolean addCurrency(Storage storage, Currency currency, BigDecimal amount) {

        String query = "insert into " + CURRENCY_TABLE + " (currency_code, storage_id, amount) values(?,?,?)";
        try (PreparedStatement statement = SQLiteConnection.getConnection().prepareStatement(query)) {

            statement.setString(1, currency.getCurrencyCode());
            statement.setLong(2, storage.getId());
            statement.setBigDecimal(3, amount);

            if (statement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteCurrency(Storage storage, Currency currency) {

        String query = "delete from " + CURRENCY_TABLE + " where storage_id=? and currency_code=?";
        try (PreparedStatement statement = SQLiteConnection.getConnection().prepareStatement(query)) {

            statement.setLong(1, storage.getId());
            statement.setString(2, currency.getCurrencyCode());

            if (statement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateAmount(Storage storage, Currency currency, BigDecimal amount) {
        String query = "update " + CURRENCY_TABLE + " set amount=? where storage_id=? and currency_code=?";

        try (PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement(query)) {
            stmt.setBigDecimal(1, amount);
            stmt.setLong(2, storage.getId());
            stmt.setString(3, currency.getCurrencyCode());

            if (stmt.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Storage> getAll() {
        storageList.clear();

        try {
            String query = "select * from " + STORAGE_TABLE;
            try (Statement statement = SQLiteConnection.getConnection().createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    DefaultStorage storage = new DefaultStorage();
                    storage.setId(resultSet.getLong("id"));
                    storage.setParentId(resultSet.getLong("parent_id"));
                    storage.setName(resultSet.getString("name"));

                    storageList.add(storage);
                }
            }

            String currencyQuery = "select * from " + CURRENCY_TABLE + " where storage_id=?";
            for (Storage storage : storageList) {
                try (PreparedStatement preparedStatement = SQLiteConnection.getConnection().prepareStatement(currencyQuery)) {
                    preparedStatement.setLong(1, storage.getId());

                    try (ResultSet rs = preparedStatement.executeQuery()) {
                        while (rs.next()) {
                            storage.addCurrency(Currency.getInstance(rs.getString("currency_code")), rs.getBigDecimal("amount"));
                        }
                    } catch (CurrencyException e) {
                        e.printStackTrace();
                    }
                }
            }

            return storageList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public Storage get(long id) {
        String query = "select * from " + STORAGE_TABLE + " where id=?";
        try (PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement(query)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                DefaultStorage storage = null;
                while (rs.next()) {
                    storage = new DefaultStorage();
                    storage.setId(id);
                    storage.setName(rs.getString("name"));
                    storage.setParentId(rs.getLong("parent_id"));
                }
                return storage;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Storage storage) throws SQLException {
        String query = "update " + STORAGE_TABLE + " set name=? where id=?";
        try (PreparedStatement statement = SQLiteConnection.getConnection().prepareStatement(query)) {

            statement.setString(1, storage.getName());
            statement.setLong(2, storage.getId());

            if (statement.executeUpdate() == 1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean delete(Storage storage) {

        String query = "delete from " + STORAGE_TABLE + " where id=?";
        try (PreparedStatement statement = SQLiteConnection.getConnection().prepareStatement(query)) {

            statement.setLong(1, storage.getId());

            if (statement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean add(Storage storage) {
        Connection connection = SQLiteConnection.getConnection();

        try {
            connection.setAutoCommit(false);
            String query = "insert into " + STORAGE_TABLE + " (name, parent_id) values(?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, storage.getName());

                if (storage.hasParent()) {
                    stmt.setLong(2, storage.getParent().getId());
                } else {
                    stmt.setNull(2, Types.BIGINT);
                }

                if (stmt.executeUpdate() == 1) {
                    try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            storage.setId(resultSet.getLong(1));
                        }
                        for (Currency currency : storage.getAvailableCurrencies()) {
                            if (!addCurrency(storage, currency, storage.getAmount(currency))) {
                                connection.rollback();
                                return false;
                            }
                        }

                        connection.commit();
                        return true;
                    }
                }
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return false;
    }
}
