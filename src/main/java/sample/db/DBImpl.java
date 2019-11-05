package sample.db;

import sample.dto.AbsItem;
import sample.dto.Customer;
import sample.dto.ItemPackage;
import sample.dto.ItemType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBImpl implements DBInterface {
    private ResultSet resultSet = null;


    @Override
    public void insertCustomer(Customer customer) {
        try (Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/abs?user=admin&password=pass")){
            String sql = "INSERT INTO customer (name, phone_number, location) VALUES (?, ?, ?)";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getAddress());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> listAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/abs?user=admin&password=pass")){
            PreparedStatement statement = connect.prepareStatement("select * from customer");
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                customers.add(new Customer(resultSet.getString("name"), resultSet.getString("phone_number"), resultSet.getString("location")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }

    @Override
    public List<Customer> findCustomer(String name) {
        List<Customer> customers = new ArrayList<>();

        try (Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/abs?user=admin&password=pass")){
            PreparedStatement statement = connect.prepareStatement("select * from customer where name like ?");
            String searchName = "%" + name + "%";
            statement.setString(1, searchName);
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                customers.add(new Customer(resultSet.getString("name"), resultSet.getString("phone_number"), resultSet.getString("location")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }

    @Override
    public void addType(ItemType type) {

    }

    @Override
    public void addPackage(ItemPackage itemPackage) {

    }

    @Override
    public void addItem(AbsItem item) {

    }

    @Override
    public List<AbsItem> listAllItems() {
        return null;
    }

    @Override
    public List<AbsItem> findItem(String name) {
        return null;
    }

    @Override
    public void updateQuantity(int quantity) {

    }

    @Override
    public void updateCost(double cost) {

    }

    @Override
    public void updatePrice(double price) {

    }
}
