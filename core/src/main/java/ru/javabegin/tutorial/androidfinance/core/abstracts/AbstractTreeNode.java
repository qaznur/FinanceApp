package ru.javabegin.tutorial.androidfinance.core.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.javabegin.tutorial.androidfinance.core.interfaces.TreeNode;

public abstract class AbstractTreeNode implements TreeNode {

    private long id;
    private String name;
    private List<TreeNode> children = new ArrayList<>();
    private TreeNode parent;
    private long parentId;

    public AbstractTreeNode() {
    }

    public AbstractTreeNode(String name) {
        this.name = name;
    }

    public AbstractTreeNode(List<TreeNode> children) {
        this.children = children;
    }

    public AbstractTreeNode(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public AbstractTreeNode(long id, String name, List<TreeNode> children, TreeNode parent) {
        this.id = id;
        this.name = name;
        this.children = children;
        this.parent = parent;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    @Override
    public void add(TreeNode child) {
        child.setParent(this);
        children.add(child);
    }

    @Override
    public void remove(TreeNode child) {
        children.remove(child);
    }

    @Override
    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public TreeNode getChild(long id) {
        for (TreeNode child : children) {
            if(child.getId() == id) {
                return child;
            }
        }
        return null;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    @Override
    public boolean hasChilds() {
        return !children.isEmpty();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTreeNode that = (AbstractTreeNode) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }
}
