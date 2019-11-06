package sample.db;

import sample.dto.AbsItem;
import sample.dto.Customer;
import sample.dto.ItemPackage;
import sample.dto.ItemType;

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

    void updateQuantity(int id, int quantity);

    void updateCost(int id, double cost);

    void updatePrice(int id, double price);
}
