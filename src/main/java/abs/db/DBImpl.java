package abs.db;

import abs.dto.*;

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
    private DBTableToList<EcuType> ecuTypeRetriever;
    private DBTableToList<Ecu> ecuRetriever;

    public DBImpl() {
        customerRetriever = new DBTableToList<>();
        itemRetriever = new DBTableToList<>();
        typeRetriever = new DBTableToList<>();
        packageRetriever = new DBTableToList<>();
        ecuTypeRetriever = new DBTableToList<>();
        ecuRetriever = new DBTableToList<>();

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

    @Override
    public int getNextEcuNumber(String likeId) {
        try {
            String sql = "select count(idecu)+1 as nextNum from ecu where absID like '" + likeId +"%';";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("nextNum");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void addEcu(Ecu ecu) {
        try {
            String sql = "INSERT INTO ecu (ecu_type_idecu_type, description, absID) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ecu.getType());
            statement.setString(2, ecu.getDescription());
            statement.setString(3, ecu.getAbsId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ecu> getEcus() {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from ecu");
            return ecuRetriever.getList(statement, new EcuBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void addEcuType(EcuType type) {
        try {
            String sql = "INSERT INTO ecu_type (name) VALUES (?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, type.getName());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EcuType> getEcuTypes() {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from ecu_type");
            return ecuTypeRetriever.getList(statement, new EcuTypeBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
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

    private static class EcuTypeBuilder implements Function<ResultSet, EcuType> {
        @Override
        public EcuType apply(ResultSet resultSet) {
            try {
                return new EcuType(resultSet.getInt("idecu_type"), resultSet.getString("name"));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private static class EcuBuilder implements Function<ResultSet, Ecu> {
        @Override
        public Ecu apply(ResultSet resultSet) {
            try {
                return new Ecu(resultSet.getInt("idecu"), resultSet.getInt("ecu_type_idecu_type"),
                        resultSet.getString("description"), resultSet.getString("absID"));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
