package ru.javabegin.tutorial.androidfinance.core.utils;

import java.util.List;

import ru.javabegin.tutorial.androidfinance.core.interfaces.TreeNode;

public class TreeUtils<T extends TreeNode> {

    public void addToTree(Long parentId, T newNode, List<T> storageList) {
        if(parentId != 0) {
            for(T currentNode: storageList) {
                if(currentNode.getId() == parentId) {
                    currentNode.add(newNode);
                } else {
                    TreeNode node = recursiveSearch(parentId, currentNode);
                    if(node != null) {
                        node.add(newNode);
                        return;
                    }
                }
            }
        } else {
            storageList.add(newNode);
        }
    }

    private TreeNode recursiveSearch(long parentId, TreeNode child) {
        for(TreeNode node: child.getChildren()) {
            if(node.getId() == parentId) {
                return node;
            } else if(node.hasChilds()) {
                recursiveSearch(parentId, node);
            }
        }
        return null;
    }

}
