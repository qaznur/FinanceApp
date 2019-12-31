package ru.javabegin.tutorial.androidfinance.core.interfaces;

import java.io.Serializable;
import java.util.List;

public interface TreeNode extends Serializable {

    long getId();

    long getParentId();

    String getName();

    void add(TreeNode child);

    void remove(TreeNode child);

    List<TreeNode> getChildren();

    TreeNode getChild(long id);

    TreeNode getParent();

    void setParent(TreeNode parent);

    boolean hasChilds();

    boolean hasParent();

    void setId(long id);

    void setName(String name);

}
