package ru.javabegin.tutorial.androidfinance.core.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OperationType {

    INCOME(1), OUTCOME(2), TRANSFER(3), CONVERT(4);

    private Integer id;
    private static Map<Integer, OperationType> map = new HashMap<>();
    private static List<OperationType> list = new ArrayList<>();

    static {
        for(OperationType type: OperationType.values()) {
            map.put(type.getId(), type);
            list.add(type);
        }
    }

    OperationType(Integer id) {
        this.id = id;
    }

    public static OperationType getType(int id) {
        return map.get(id);
    }

    public static List<OperationType> getList() {
        return list;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
