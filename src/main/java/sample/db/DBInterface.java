package sample.db;

import sample.dto.AbsItem;
import sample.dto.Customer;
import sample.dto.ItemPackage;
import sample.dto.ItemType;

import java.util.List;

public interface DBInterface {
    void insertCustomer(Customer customer);
    List<Customer> listAllCustomers();
    List<Customer> findCustomer(String name);

    void addType(ItemType type);

    void addPackage(ItemPackage itemPackage);

    void addItem(AbsItem item);
    List<AbsItem> listAllItems();
    List<AbsItem> findItem(String name);
    void updateQuantity(int quantity);
    void updateCost(double cost);
    void updatePrice(double price);
}
