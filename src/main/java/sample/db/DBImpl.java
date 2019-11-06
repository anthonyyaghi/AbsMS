package sample.db;

import sample.dto.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DBImpl implements DBInterface {
    private Connection connection;

    private DBTableToList<Customer> customerRetriever;
    private DBTableToList<AbsItem> itemRetriever;
    private DBTableToList<ItemType> typeRetriever;
    private DBTableToList<ItemPackage> packageRetriever;

    public DBImpl() {
        customerRetriever = new DBTableToList<>();
        itemRetriever = new DBTableToList<>();
        typeRetriever = new DBTableToList<>();
        packageRetriever = new DBTableToList<>();

        try {
            connection = ConnectionProvider.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addCustomer(Customer customer) {
        try {
            String sql = "INSERT INTO customer (name, phone_number, location) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getAddress());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getCustomers() {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from customer");
            return customerRetriever.getList(statement, new CustomerBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Customer> findCustomer(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from customer where name like ?");
            statement.setString(1, "%" + name + "%");
            return customerRetriever.getList(statement, new CustomerBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void addType(ItemType type) {
        try {
            String sql = "INSERT INTO type (name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, type.getName());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ItemType> getTypes() {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from type");
            return typeRetriever.getList(statement, new TypeBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void addPackage(ItemPackage itemPackage) {
        try {
            String sql = "INSERT INTO package (name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, itemPackage.getName());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ItemPackage> getPackages() {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from package");
            return packageRetriever.getList(statement, new PackageBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void addItem(AbsItem item) {
        try {
            String sql = "INSERT INTO item (name, package_idpackage, type_idtype, cost, selling_price, quantity) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, item.getName());
            statement.setInt(2, item.getPackageId());
            statement.setInt(3, item.getTypeId());
            statement.setDouble(4, item.getCost());
            statement.setDouble(5, item.getPrice());
            statement.setInt(6, item.getQuantity());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AbsItem> getItems() {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from item");
            return itemRetriever.getList(statement, new ItemBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<AbsItem> findItem(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from item where name like ?");
            statement.setString(1, "%" + name + "%");
            return itemRetriever.getList(statement, new ItemBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void updateQuantity(int id, int quantity) {
        try {
            String sql = "update item set quantity = quantity" + (quantity > 0 ? "+" : "") + quantity + " where iditem = " + id;
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCost(int id, double cost) {
        try {
            String sql = "update item set cost = " + cost + " where iditem = " + id;
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePrice(int id, double price) {
        try {
            String sql = "update item set selling_price = " + price + " where iditem = " + id;
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //    ------------------------------------------------------------------------------------------------------------------
    private static class TypeBuilder implements Function<ResultSet, ItemType> {
        @Override
        public ItemType apply(ResultSet resultSet) {
            try {
                return new ItemType(resultSet.getInt("idtype"), resultSet.getString("name"));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private static class PackageBuilder implements Function<ResultSet, ItemPackage> {
        @Override
        public ItemPackage apply(ResultSet resultSet) {
            try {
                return new ItemPackage(resultSet.getInt("idpackage"), resultSet.getString("name"));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private static class CustomerBuilder implements Function<ResultSet, Customer> {

        @Override
        public Customer apply(ResultSet resultSet) {
            try {
                return new Customer(resultSet.getString("name"), resultSet.getString("phone_number"), resultSet.getString("location"));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private static class ItemBuilder implements Function<ResultSet, AbsItem> {
        @Override
        public AbsItem apply(ResultSet resultSet) {
            try {
                return new AbsItem(resultSet.getInt("iditem"), resultSet.getString("name"),
                        resultSet.getInt("quantity"), resultSet.getDouble("cost"), resultSet.getDouble("selling_price"),
                        resultSet.getInt("package_idpackage"), resultSet.getInt("type_idtype"));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
