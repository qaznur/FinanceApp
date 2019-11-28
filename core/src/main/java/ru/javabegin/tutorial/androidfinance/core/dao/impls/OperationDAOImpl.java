package ru.javabegin.tutorial.androidfinance.core.dao.impls;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.javabegin.tutorial.androidfinance.core.abstracts.AbstractOperation;
import ru.javabegin.tutorial.androidfinance.core.dao.interfaces.OperationDAO;
import ru.javabegin.tutorial.androidfinance.core.database.SQLiteConnection;
import ru.javabegin.tutorial.androidfinance.core.impls.operations.ConvertOperation;
import ru.javabegin.tutorial.androidfinance.core.impls.operations.IncomeOperation;
import ru.javabegin.tutorial.androidfinance.core.impls.operations.OutcomeOperation;
import ru.javabegin.tutorial.androidfinance.core.impls.operations.TransferOperation;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Operation;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Storage;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public class OperationDAOImpl implements OperationDAO {

    private final static String OPERATION_TABLE = "operation";
    private List<Operation> operationList = new ArrayList<>();

    private Map<Long, Source> sourceIdentityMap;
    private Map<Long, Storage> storageIdentityMap;

    public OperationDAOImpl(Map<Long, Source> sourceIdentityMap, Map<Long, Storage> storageIdentityMap) {
        this.sourceIdentityMap = sourceIdentityMap;
        this.storageIdentityMap = storageIdentityMap;
    }


    @Override
    public List<Operation> getList(OperationType operationType) {
        operationList.clear();

        String query = "select * from " + OPERATION_TABLE + " where type_id=?";
        try(PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement(query)) {
            stmt.setLong(1, operationType.getId());
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    operationList.add(fillOperation(rs));
                }
                return operationList;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Operation> getAll() {
        operationList.clear();

        try (Statement stmt = SQLiteConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("select * from " + OPERATION_TABLE)) {

            while (rs.next()) {
                operationList.add(fillOperation(rs));
            }

            return operationList;// должен содержать только корневые элементы
        } catch (SQLException e) {
            Logger.getLogger(OperationDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

    @Override
    public Operation get(long id) {
        String query = "select * from " + OPERATION_TABLE + " where id=?";
        try (PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return fillOperation(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AbstractOperation fillOperation(ResultSet rs) throws SQLException {
        OperationType operationType = OperationType.getType(rs.getInt("type_id"));
        AbstractOperation operation = createOperation(operationType, rs);

        if (operation == null) {
            return null;
        }

        operation.setId(rs.getLong("id"));
        operation.setOperationType(operationType);
        Calendar datetime = Calendar.getInstance();
        datetime.setTimeInMillis(rs.getLong("datetime"));
        operation.setDateTime(datetime);
        operation.setDescription(rs.getString("description"));

        return operation;
    }

    private AbstractOperation createOperation(OperationType operationType, ResultSet rs) throws SQLException {
        switch (operationType) {

            case INCOME: {
                IncomeOperation operation = new IncomeOperation();
                operation.setFromSource(sourceIdentityMap.get(rs.getLong("from_source_id")));
                operation.setToStorage(storageIdentityMap.get(rs.getLong("to_storage_id")));
                operation.setFromCurrency(Currency.getInstance(rs.getString("from_currency_code")));
                operation.setFromAmount(rs.getBigDecimal("from_amount"));

                return operation;
            }
            case OUTCOME: {
                OutcomeOperation operation = new OutcomeOperation();
                operation.setToSource(sourceIdentityMap.get(rs.getLong("to_source_id")));
                operation.setFromStorage(storageIdentityMap.get(rs.getLong("from_storage_id")));
                operation.setFromAmount(rs.getBigDecimal("from_amount"));
                operation.setFromCurrency(Currency.getInstance(rs.getString("from_currency_code")));

                return operation;
            }
            case CONVERT: {
                ConvertOperation operation = new ConvertOperation();
                operation.setFromStorage(storageIdentityMap.get(rs.getLong("from_storage_id")));
                operation.setFromAmount(rs.getBigDecimal("from_amount"));
                operation.setFromCurrency(Currency.getInstance(rs.getString("from_currency_code")));
                operation.setToStorage(storageIdentityMap.get(rs.getLong("to_storage_id")));
                operation.setToAmount(rs.getBigDecimal("to_amount"));
                operation.setToCurrency(Currency.getInstance(rs.getString("to_currency_code")));

                return operation;
            }
            case TRANSFER:
                TransferOperation operation = new TransferOperation();
                operation.setFromStorage(storageIdentityMap.get(rs.getLong("from_storage_id")));
                operation.setToStorage(storageIdentityMap.get(rs.getLong("to_storage_id")));
                operation.setFromAmount(rs.getBigDecimal("from_storage_id"));
                operation.setFromCurrency(Currency.getInstance(rs.getString("from_currency_code")));

                return operation;
        }

        return null;
    }

    @Override
    public boolean update(Operation operation) {
        return false;
    }

    @Override
    public boolean delete(Operation operation) {
        String query = "delete from " + OPERATION_TABLE + " where id=?";
        try (PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement(query)) {
            stmt.setLong(1, operation.getId());

            if (stmt.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(Operation operation) {
        String query = createSql(operation.getOperationType());

        try (PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, operation.getDateTime().getTimeInMillis());
            stmt.setLong(2, operation.getOperationType().getId());
            stmt.setString(3, operation.getDescription());

            switch (operation.getOperationType()) {
                case INCOME:
                    IncomeOperation incomeOperation = ((IncomeOperation) operation);

                    stmt.setLong(4, incomeOperation.getFromSource().getId());
                    stmt.setLong(5, incomeOperation.getToStorage().getId());
                    stmt.setString(6, incomeOperation.getFromCurrency().getCurrencyCode());
                    stmt.setBigDecimal(7, incomeOperation.getFromAmount());

                    break;

                case OUTCOME:
                    OutcomeOperation outcomeOperation = ((OutcomeOperation) operation);

                    stmt.setLong(4, outcomeOperation.getFromStorage().getId());
                    stmt.setLong(5, outcomeOperation.getToSource().getId());
                    stmt.setString(6, outcomeOperation.getFromCurrency().getCurrencyCode());
                    stmt.setBigDecimal(7, outcomeOperation.getFromAmount());

                    break;

                case TRANSFER:
                    TransferOperation transferOperation = ((TransferOperation) operation);

                    stmt.setLong(4, transferOperation.getFromStorage().getId());
                    stmt.setLong(5, transferOperation.getToStorage().getId());
                    stmt.setString(6, transferOperation.getFromCurrency().getCurrencyCode());
                    stmt.setBigDecimal(7, transferOperation.getFromAmount());

                    break;

                case CONVERT:
                    ConvertOperation convertOperation = ((ConvertOperation) operation);

                    stmt.setLong(4, convertOperation.getFromStorage().getId());
                    stmt.setLong(5, convertOperation.getToStorage().getId());
                    stmt.setString(6, convertOperation.getFromCurrency().getCurrencyCode());
                    stmt.setBigDecimal(7, convertOperation.getFromAmount());
                    stmt.setString(8, convertOperation.getToCurrency().getCurrencyCode());
                    stmt.setBigDecimal(9, convertOperation.getToAmount());

                    break;

            }

            if (stmt.executeUpdate() == 1) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        operation.setId(rs.getLong(1));
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String createSql(OperationType operationType) {
        StringBuilder sb = new StringBuilder("insert into " + OPERATION_TABLE + "(datetime, type_id, description, ");
        switch (operationType) {
            case INCOME:
                return sb.append("from_source_id, to_storage_id, from_currency_code, from_amount) values(?,?,?,?,?,?,?)").toString();
            case OUTCOME:
                return sb.append("from_storage_id, to_source_id, from_currency_code, from_amount) values(?,?,?,?,?,?,?)").toString();
            case TRANSFER:
                return sb.append("from_storage_id, to_storage_id, from_currency_code, from_amount) values(?,?,?,?,?,?,?)").toString();
            case CONVERT:
                return sb.append("from_storage_id, to_storage_id, from_currency_code, from_amount, to_currency_code, to_amount) values(?,?,?,?,?,?,?,?,?)").toString();
        }

        return null;
    }
}
