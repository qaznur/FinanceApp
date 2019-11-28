package ru.javabegin.tutorial.androidfinance.core.impls;

import java.util.List;

import ru.javabegin.tutorial.androidfinance.core.abstracts.AbstractTreeNode;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.interfaces.TreeNode;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public class DefaultSource extends AbstractTreeNode implements Source {

    private OperationType operationType;

    public DefaultSource() {
    }

    public DefaultSource(String name) {
        super(name);
    }

    public DefaultSource(List<TreeNode> children) {
        super(children);
    }

    public DefaultSource(long id, String name) {
        super(id, name);
    }

    public DefaultSource(long id, String name, List<TreeNode> children, TreeNode parent) {
        super(id, name, children, parent);
    }

    public DefaultSource(long id, String name, OperationType operationType) {
        super(id, name);
        this.operationType = operationType;
    }

    @Override
    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public void add(TreeNode child) {
        if(child instanceof DefaultSource) {
            ((DefaultSource) child).setOperationType(operationType);
        }

        super.add(child);
    }

    @Override
    public void setParent(TreeNode parent) {
        if(parent instanceof DefaultSource) {
            operationType = ((DefaultSource) parent).getOperationType();
        }
        super.setParent(parent);
    }
}
