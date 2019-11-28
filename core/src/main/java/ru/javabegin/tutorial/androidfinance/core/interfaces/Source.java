package ru.javabegin.tutorial.androidfinance.core.interfaces;

import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public interface Source extends TreeNode{

    OperationType getOperationType();

}
