package abs.db;

import abs.dto.*;

import java.util.List;

public interface DBInterface {
    void addCustomer(Customer customer);

    List<Customer> getCustomers();

    List<Customer> findCustomer(String name);

    void addType(ItemType type);

    List<ItemType> getTypes();

    void addPackage(ItemPackage itemPackage);

    List<ItemPackage> getPackages();

    void addItem(AbsItem item);

    List<AbsItem> getItems();

    List<AbsItem> findItem(String name);

    AbsItem getItemById(int id);

    void updateQuantity(int id, int quantity);

    void updateCost(int id, double cost);

    void updatePrice(int id, double price);

    int getNextEcuNumber(String likeId);

    void addEcu(Ecu ecu);

    List<Ecu> getEcus();

    List<Ecu> findEcus(String query);

    void addEcuType(EcuType type);

    List<EcuType> getEcuTypes();

    void addTransaction(Transaction transaction);

    List<Transaction> getTransactions();

    List<Transaction> findTransactions(String query);

    Transaction getTransactionById(int id);

    void updateTransaction(Transaction transaction);

    List<AbsItem> getAvailableItems(int transactionId);

    List<AbsItem> getUsedItems(int transactionId);

    void addItemToTransaction(int itemId, int transactionId);

    void removeItemFromTransaction(int itemId, int transactionId);

    void updateItemUsage(int itemId, int transactionId, int quantity);
}
