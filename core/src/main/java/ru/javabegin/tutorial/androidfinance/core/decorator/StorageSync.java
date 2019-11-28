package ru.javabegin.tutorial.androidfinance.core.decorator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.javabegin.tutorial.androidfinance.core.dao.interfaces.StorageDAO;
import ru.javabegin.tutorial.androidfinance.core.exceptions.AmountException;
import ru.javabegin.tutorial.androidfinance.core.exceptions.CurrencyException;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Storage;
import ru.javabegin.tutorial.androidfinance.core.utils.TreeUtils;

public class StorageSync implements StorageDAO {

    private StorageDAO storageDAO;
    private TreeUtils<Storage> treeUtils = new TreeUtils<>();

    private List<Storage> treeList = new ArrayList<>();
    private Map<Long, Storage> idendityMap = new HashMap<>();

    public StorageSync(StorageDAO storageDAO) {
        this.storageDAO = storageDAO;
        init();
    }

    public Map<Long, Storage> getIdendityMap() {
        return idendityMap;
    }

    private void init() {
        List<Storage> storageList = storageDAO.getAll();

        for (Storage storage : storageList) {
            idendityMap.put(storage.getId(), storage);
            treeUtils.addToTree(storage.getParentId(), storage, treeList);
        }
    }

    @Override
    public boolean addCurrency(Storage storage, Currency currency, BigDecimal amount) throws CurrencyException {
        if (storageDAO.addCurrency(storage, currency, amount)) {
            storage.addCurrency(currency, amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCurrency(Storage storage, Currency currency) throws CurrencyException {
        if (storageDAO.deleteCurrency(storage, currency)) {
            storage.deleteCurrency(currency);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAmount(Storage storage, Currency currency, BigDecimal amount) {
        if (storageDAO.updateAmount(storage, currency, amount)) {
            try {
                storage.updateAmount(amount, currency);
            } catch (AmountException e) {
                e.printStackTrace();
            } catch (CurrencyException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Storage> getAll() {
        return treeList;
    }

    @Override
    public Storage get(long id) {
        return idendityMap.get(id);
    }

    @Override
    public boolean update(Storage storage) {
        if (storageDAO.update(storage)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Storage storage) {
        if (storageDAO.delete(storage)) {
            removeFromCollections(storage);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(Storage storage) {
        if(storageDAO.add(storage)) {
            addToCollections(storage);
            return true;
        } else {

        }
        return false;
    }

    private void addToCollections(Storage storage) {
        idendityMap.put(storage.getId(), storage);

        if(storage.hasParent()) {
            storage.getParent().add(storage);
        } else {
            treeList.add(storage);
        }
    }

    private void removeFromCollections(Storage storage) {
        idendityMap.put(storage.getId(), storage);

        if(storage.hasParent()) {
            storage.getParent().remove(storage);
        } else {
            treeList.remove(storage);
        }
    }

}
