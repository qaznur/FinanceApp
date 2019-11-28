package ru.javabegin.tutorial.androidfinance.core.decorator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.javabegin.tutorial.androidfinance.core.dao.interfaces.OperationDAO;
import ru.javabegin.tutorial.androidfinance.core.exceptions.CurrencyException;
import ru.javabegin.tutorial.androidfinance.core.impls.operations.ConvertOperation;
import ru.javabegin.tutorial.androidfinance.core.impls.operations.IncomeOperation;
import ru.javabegin.tutorial.androidfinance.core.impls.operations.OutcomeOperation;
import ru.javabegin.tutorial.androidfinance.core.impls.operations.TransferOperation;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Operation;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public class OperationSync implements OperationDAO {

    private OperationDAO operationDAO;

    private List<Operation> operationList;
    private Map<OperationType, List<Operation>> operationMap = new EnumMap<>(OperationType.class);
    private Map<Long, Operation> identityMap = new HashMap<>();

    private SourceSync sourceSync;
    private StorageSync storageSync;

    public OperationSync(OperationDAO operationDAO, SourceSync sourceSync, StorageSync storageSync) {
        this.operationDAO = operationDAO;
        this.sourceSync = sourceSync;
        this.storageSync = storageSync;

        init();
    }

    private void init() {
        operationList = operationDAO.getAll();

        for (Operation operation : operationList) {
            identityMap.put(operation.getId(), operation);
        }

        fillOperationMap();
    }

    private void fillOperationMap() {
        for (OperationType type : OperationType.values()) {
            List<Operation> list = new ArrayList<>();
            for (Operation operation : operationList) {
                if (operation.getOperationType() == type) {
                    list.add(operation);
                }
                operationMap.put(type, list);
            }

        }
    }

    @Override
    public List<Operation> getList(OperationType operationType) {
        return operationMap.get(operationType);
    }

    @Override
    public List<Operation> getAll() {
        return operationList;
    }

    @Override
    public Operation get(long id) {
        return identityMap.get(id);
    }

    @Override
    public boolean update(Operation operation) {
        if (operationDAO.update(operation)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Operation operation) {
        if (operationDAO.delete(operation) && revertBalance(operation)) {
            removeFromCollections(operation);
        }
        return false;
    }

    private boolean revertBalance(Operation operation) {
        boolean updateAmountResult = false;

        try {
            switch (operation.getOperationType()) {
                case INCOME: {
                    IncomeOperation incomeOperation = (IncomeOperation) operation;

                    BigDecimal currentAmount = incomeOperation.getToStorage().getAmount(incomeOperation.getFromCurrency());
                    BigDecimal newAmount = currentAmount.subtract(incomeOperation.getFromAmount());
                    updateAmountResult = storageSync.updateAmount(incomeOperation.getToStorage(), incomeOperation.getFromCurrency(), newAmount);

                    break;
                }
                case OUTCOME: {
                    OutcomeOperation outcomeOperation = (OutcomeOperation) operation;

                    BigDecimal currentAmount = outcomeOperation.getFromStorage().getAmount(outcomeOperation.getFromCurrency());
                    BigDecimal newAmount = currentAmount.add(outcomeOperation.getFromAmount());
                    updateAmountResult = storageSync.updateAmount(outcomeOperation.getFromStorage(), outcomeOperation.getFromCurrency(), newAmount);

                    break;
                }
                case TRANSFER: {
                    TransferOperation transferOperation = (TransferOperation) operation;

                    BigDecimal currentAmountFromStorage = transferOperation.getFromStorage().getAmount(transferOperation.getFromCurrency());
                    BigDecimal newAmountFromStorage = currentAmountFromStorage.add(transferOperation.getFromAmount());

                    BigDecimal currentAmountToStorage = transferOperation.getToStorage().getAmount(transferOperation.getFromCurrency());
                    BigDecimal newAmountToStorage = currentAmountToStorage.subtract(transferOperation.getFromAmount());

                    updateAmountResult = storageSync.updateAmount(transferOperation.getFromStorage(), transferOperation.getFromCurrency(), newAmountFromStorage) &&
                            storageSync.updateAmount(transferOperation.getToStorage(), transferOperation.getFromCurrency(), newAmountToStorage);

                    break;
                }
                case CONVERT: {
                    ConvertOperation convertOperation = (ConvertOperation) operation;

                    BigDecimal currentAmountFromStorage = convertOperation.getFromStorage().getAmount(convertOperation.getFromCurrency());
                    BigDecimal newAmountFromStorage = currentAmountFromStorage.add(convertOperation.getFromAmount());

                    BigDecimal currentAmountToStorage = convertOperation.getToStorage().getAmount(convertOperation.getToCurrency());
                    BigDecimal newAmountToStorage = currentAmountToStorage.subtract(convertOperation.getToAmount());

                    updateAmountResult = storageSync.updateAmount(convertOperation.getFromStorage(), convertOperation.getFromCurrency(), newAmountFromStorage) &&
                            storageSync.updateAmount(convertOperation.getToStorage(), convertOperation.getToCurrency(), newAmountToStorage);

                    break;
                }
            }
        } catch (CurrencyException e) {
            e.printStackTrace();
        }
        if (!updateAmountResult) {
            delete(operation);
            return false;
        }
        return true;
    }

    private void addToCollections(Operation operation) {
        operationList.add(operation);
        identityMap.put(operation.getId(), operation);
        operationDAO.getList(operation.getOperationType()).add(operation);
    }

    private void removeFromCollections(Operation operation) {
        operationList.remove(operation);
        identityMap.remove(operation.getId());
        operationDAO.getList(operation.getOperationType()).remove(operation);
    }

    @Override
    public boolean add(Operation operation) {
        if(operationDAO.add(operation)) {
            addToCollections(operation);

            boolean updateAmountResult = false;

            try {
                switch (operation.getOperationType()) {
                    case INCOME: {
                        IncomeOperation incomeOperation = (IncomeOperation) operation;

                        BigDecimal currentAmount = incomeOperation.getToStorage().getAmount(incomeOperation.getFromCurrency());
                        BigDecimal newAmount = currentAmount.add(incomeOperation.getFromAmount());
                        updateAmountResult = storageSync.updateAmount(incomeOperation.getToStorage(), incomeOperation.getFromCurrency(), newAmount);

                        break;
                    }
                    case OUTCOME: {
                        OutcomeOperation outcomeOperation = (OutcomeOperation) operation;

                        BigDecimal currentAmount = outcomeOperation.getFromStorage().getAmount(outcomeOperation.getFromCurrency());
                        BigDecimal newAmount = currentAmount.subtract(outcomeOperation.getFromAmount());
                        updateAmountResult = storageSync.updateAmount(outcomeOperation.getFromStorage(), outcomeOperation.getFromCurrency(), newAmount);

                        break;
                    }
                    case TRANSFER: {
                        TransferOperation transferOperation = (TransferOperation) operation;

                        BigDecimal currentAmountFromStorage = transferOperation.getFromStorage().getAmount(transferOperation.getFromCurrency());
                        BigDecimal newAmountFromStorage = currentAmountFromStorage.subtract(transferOperation.getFromAmount());

                        BigDecimal currentAmountToStorage = transferOperation.getToStorage().getAmount(transferOperation.getFromCurrency());
                        BigDecimal newAmountToStorage = currentAmountToStorage.add(transferOperation.getFromAmount());

                        updateAmountResult = storageSync.updateAmount(transferOperation.getFromStorage(), transferOperation.getFromCurrency(), newAmountFromStorage) &&
                                storageSync.updateAmount(transferOperation.getToStorage(), transferOperation.getFromCurrency(), newAmountToStorage);

                        break;
                    }
                    case CONVERT: {
                        ConvertOperation convertOperation = (ConvertOperation) operation;

                        BigDecimal currentAmountFromStorage = convertOperation.getFromStorage().getAmount(convertOperation.getFromCurrency());
                        BigDecimal newAmountFromStorage = currentAmountFromStorage.subtract(convertOperation.getFromAmount());

                        BigDecimal currentAmountToStorage = convertOperation.getToStorage().getAmount(convertOperation.getToCurrency());
                        BigDecimal newAmountToStorage = currentAmountToStorage.add(convertOperation.getToAmount());

                        updateAmountResult = storageSync.updateAmount(convertOperation.getFromStorage(), convertOperation.getFromCurrency(), newAmountFromStorage) &&
                                storageSync.updateAmount(convertOperation.getToStorage(), convertOperation.getToCurrency(), newAmountToStorage);

                        break;
                    }
                }
            } catch (CurrencyException e) {
                e.printStackTrace();
            }

            if(!updateAmountResult) {
                delete(operation);
                return false;
            }
            return true;
        }
        return false;
    }

}
