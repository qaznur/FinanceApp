package ru.javabegin.tutorial.androidfinance.core.start;

import ru.javabegin.tutorial.androidfinance.core.abstracts.Person;
import ru.javabegin.tutorial.androidfinance.core.decorator.SourceSync;
import ru.javabegin.tutorial.androidfinance.core.impls.DefaultSource;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public class Start {

    public static void main(String[] args) {

//        StorageSync storageSync = new StorageSync(new StorageDAOImpl());
//        SourceSync sourceSync = new SourceSync(new SourceDAOImpl());
//        OperationSync operationSync = new OperationSync(new OperationDAOImpl(sourceSync.getIdentityMap(),
//                storageSync.getIdendityMap()), sourceSync, storageSync);
//
////        testSource(sourceSync);
//
//        Storage parentStorage = storageSync.get(4);
//        DefaultStorage defaultStorage = new DefaultStorage("def storage");
//        try{
//            defaultStorage.addCurrency(Currency.getInstance("USD"), new BigDecimal(220));
//            defaultStorage.addCurrency(Currency.getInstance("KZT"), new BigDecimal(350));
//
//            defaultStorage.setParent(parentStorage);
//            storageSync.add(defaultStorage);
//        } catch (CurrencyException e) {
//            e.printStackTrace();
//        }


//        IncomeOperation incomeOperation = new IncomeOperation();
//        incomeOperation.setFromSource(source);
//        incomeOperation.setToStorage(storage);
//        incomeOperation.setFromAmount(new BigDecimal(500));
//        incomeOperation.setFromCurrency(Currency.getInstance());


        Person person = new Person("Nuradil", 23);
        Person person2 = person;

        person.setName("Baho");
//        print(person);
        System.out.println("person = " + person2.toString());
    }

    static void print(Person p) {
        p.setName("Kudri");
        p.setAge(24);
    }

    private static void testSource(SourceSync sourceSync) {
        Source parentSource = sourceSync.get(12);

        DefaultSource defaultSource = new DefaultSource("test source");
        defaultSource.setOperationType(OperationType.OUTCOME);
        defaultSource.setParent(parentSource);
        sourceSync.add(defaultSource);
        System.out.println("sourceSync = " + sourceSync.getAll());
    }
}
