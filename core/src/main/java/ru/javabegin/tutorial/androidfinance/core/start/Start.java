package ru.javabegin.tutorial.androidfinance.core.start;

import java.math.BigDecimal;
import java.util.Currency;

import ru.javabegin.tutorial.androidfinance.core.dao.impls.OperationDAOImpl;
import ru.javabegin.tutorial.androidfinance.core.dao.impls.SourceDAOImpl;
import ru.javabegin.tutorial.androidfinance.core.dao.impls.StorageDAOImpl;
import ru.javabegin.tutorial.androidfinance.core.decorator.OperationSync;
import ru.javabegin.tutorial.androidfinance.core.decorator.SourceSync;
import ru.javabegin.tutorial.androidfinance.core.decorator.StorageSync;
import ru.javabegin.tutorial.androidfinance.core.impls.operations.IncomeOperation;

public class Start {

    public static void main(String[] args) {

        StorageSync storageSync = new StorageSync(new StorageDAOImpl());
        SourceSync sourceSync = new SourceSync(new SourceDAOImpl());
        OperationSync operationSync = new OperationSync(new OperationDAOImpl(sourceSync.getIdentityMap(),
                storageSync.getIdendityMap()), sourceSync, storageSync);


//        Storage storage = storageSync.get(7);
//        Source source = sourceSync.get(5);

//        OutcomeOperation outcomeOperation = new OutcomeOperation();
//        outcomeOperation.setFromStorage(storage);
//        outcomeOperation.setToSource(source);
//        outcomeOperation.setFromAmount(new BigDecimal(300));
//        outcomeOperation.setFromCurrency(Currency.getInstance("KZT"));
//        outcomeOperation.setDateTime(Calendar.getInstance());
//        outcomeOperation.setDescription("out test desc");
//
//        operationSync.add(outcomeOperation);
//        System.out.println("outcomeOperation = " + outcomeOperation);


//        IncomeOperation incomeOperation = new IncomeOperation();
//        incomeOperation.setFromCurrency(Currency.getInstance("KZT"));
//        incomeOperation.setFromAmount(BigDecimal.valueOf(250));
//        incomeOperation.setToStorage(storage);
//        incomeOperation.setFromSource(source);
//        incomeOperation.setDateTime(Calendar.getInstance());
//        incomeOperation.setDescription("test in oper");
//
//        operationSync.add(incomeOperation);
//        System.out.println("incomeOperation = " + incomeOperation);

//        Storage toStorage = storageSync.get(9);
//        Storage fromStorage = storageSync.get(6);
//
//        TransferOperation transferOperation = new TransferOperation();
//        transferOperation.setToStorage(toStorage);
//        transferOperation.setFromStorage(fromStorage);
//        transferOperation.setFromCurrency(Currency.getInstance("RUB"));
//        transferOperation.setFromAmount(BigDecimal.valueOf(10));
//        transferOperation.setDateTime(Calendar.getInstance());
//        transferOperation.setDescription("transfer oper");
//        operationSync.add(transferOperation);
//        System.out.println("incomeOperation = " + transferOperation);

//        ConvertOperation convertOperation = new ConvertOperation();
//        convertOperation.setToCurrency(Currency.getInstance("RUB"));
//        convertOperation.setFromCurrency(Currency.getInstance("KZT"));
//        convertOperation.setToAmount(BigDecimal.valueOf(50));
//        convertOperation.setFromAmount(BigDecimal.valueOf(200));
//        convertOperation.setToStorage(toStorage);
//        convertOperation.setFromStorage(fromStorage);
//        convertOperation.setDateTime(Calendar.getInstance());
//        convertOperation.setDescription("convert test");
//        operationSync.add(convertOperation);
//        System.out.println("convertOperation = " + convertOperation);


//        operationSync.delete(operationSync.get(11));

        ((IncomeOperation) operationSync.get(18)).setFromAmount(BigDecimal.valueOf(150));
        ((IncomeOperation) operationSync.get(18)).setFromCurrency(Currency.getInstance("RUB"));
        ((IncomeOperation) operationSync.get(18)).setToStorage(storageSync.get(6));
        operationSync.update(operationSync.get(18));
        operationSync.getAll();

    }


}
