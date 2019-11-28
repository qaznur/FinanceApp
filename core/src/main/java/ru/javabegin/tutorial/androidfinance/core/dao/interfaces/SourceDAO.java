package ru.javabegin.tutorial.androidfinance.core.dao.interfaces;

import java.util.List;

import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public interface SourceDAO extends CommonDAO<Source> {

    List<Source> getList(OperationType operationType);

}
