package ru.javabegin.tutorial.androidfinance.core.dao.interfaces;


import java.util.List;

import ru.javabegin.tutorial.androidfinance.core.interfaces.Operation;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public interface OperationDAO extends CommonDAO<Operation> {

    List<Operation> getList(OperationType operationType);

}
