package ru.javabegin.tutorial.androidfinance.core.decorator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.javabegin.tutorial.androidfinance.core.dao.interfaces.SourceDAO;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;
import ru.javabegin.tutorial.androidfinance.core.utils.TreeUtils;

public class SourceSync implements SourceDAO {

    private TreeUtils<Source> treeUtils = new TreeUtils<>();
    private SourceDAO sourceDAO;

    private List<Source> treeList = new ArrayList<>();
    private Map<OperationType, List<Source>> sourceMap = new EnumMap<>(OperationType.class);
    private Map<Long, Source> identityMap = new HashMap<>();

    public SourceSync(SourceDAO sourceDAO) {
        this.sourceDAO = sourceDAO;
        init();
    }

    public Map<Long, Source> getIdentityMap() {
        return identityMap;
    }

    private void init() {
        List<Source> sourceList = sourceDAO.getAll();
        for (Source source : sourceList) {
            identityMap.put(source.getId(), source);
            treeUtils.addToTree(source.getParentId(), source, treeList);
        }
        fillSourceMap();
    }

    private void fillSourceMap() {
//        for (final OperationType type : OperationType.values()) {
//            sourceMap.put(type, list.stream().filter(source -> source.getOperationType() == type)
//                    .collect(Collectors.toList()));
//        }

        for (OperationType type : OperationType.values()) {
            List<Source> list = new ArrayList<>();
            for (Source source : treeList) {
                if (source.getOperationType() == type) {
                    list.add(source);
                }
            }
            sourceMap.put(type, list);
        }
    }

    public SourceDAO getSourceDAO() {
        return sourceDAO;
    }

    @Override
    public List<Source> getList(OperationType operationType) {
        return sourceMap.get(operationType);
    }

    @Override
    public List<Source> getAll() {
        return treeList;
    }

    @Override
    public Source get(long id) {
        return identityMap.get(id);
    }

    @Override
    public boolean update(Source source) throws SQLException {
        if (sourceDAO.update(source)) {
            identityMap.get(source.getId()).setName(source.getName());
            // TODO также обновлять иконку
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Source source) throws SQLException {
        if (sourceDAO.delete(source)) {
            removeFromCollections(source);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(Source source) throws SQLException {
        if (sourceDAO.add(source)) {
            addToCollections(source);
            return true;
        }
        return false;
    }

    private void addToCollections(Source source) {
        identityMap.put(source.getId(), source);

        if (source.hasParent()) {
            if (!source.getParent().getChildren().contains(source)) {
                source.getParent().add(source);
            }
        } else {
            treeList.add(source);
            sourceMap.get(source.getOperationType()).add(source);
        }
    }

    private void removeFromCollections(Source source) {
        identityMap.remove(source.getId());

        if (source.hasParent()) {
            source.getParent().remove(source);
        } else {
            sourceMap.get(source.getOperationType()).remove(source);
            treeList.remove(source);
        }
    }
}
