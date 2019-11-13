package abs.db;

import abs.dto.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private DBTableToList<Transaction> transactionRetriever;

    public DBImpl() {
        customerRetriever = new DBTableToList<>();
        itemRetriever = new DBTableToList<>();
        typeRetriever = new DBTableToList<>();
        packageRetriever = new DBTableToList<>();
        ecuTypeRetriever = new DBTableToList<>();
        ecuRetriever = new DBTableToList<>();
        transactionRetriever = new DBTableToList<>();

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
            String query = "select item.*, package.name as packageName, type.name typeName from item, package, type " +
                    "where package_idpackage = idpackage and type_idtype=idtype";
            PreparedStatement statement = connection.prepareStatement(query);
            return itemRetriever.getList(statement, new ItemBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<AbsItem> findItem(String name) {
        try {
            String query = "select item.*, package.name as packageName, type.name typeName from item, package, type " +
                    "where package_idpackage = idpackage and type_idtype=idtype and name like ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + name + "%");
            return itemRetriever.getList(statement, new ItemBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public AbsItem getItemById(int id) {
        try {
            String query = "select item.*, package.name as packageName, type.name typeName from item, package, type " +
                    "where package_idpackage = idpackage and type_idtype=idtype and iditem = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            List<AbsItem> items = itemRetriever.getList(statement, new ItemBuilder());
            if (items.size() != 1) {
                return null;
            }
            return items.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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
            String sql = "select count(idecu)+1 as nextNum from ecu where absID like '" + likeId + "%';";
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
    public List<Ecu> findEcus(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from ecu where absID like ? OR description like ?");
            String sqlParam = "%" + query + "%";
            statement.setString(1, sqlParam);
            statement.setString(2, sqlParam);
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

    @Override
    public void addTransaction(Transaction transaction) {
        try {
            String sql = "INSERT INTO transaction (ecu_idecu, indate, payment, description, outdate, customer_idcustomer, medium, technician) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, transaction.getEcu_id());
            statement.setDate(2, transaction.getInDate());
            statement.setDouble(3, transaction.getPayment());
            statement.setString(4, transaction.getDescription());
            statement.setDate(5, transaction.getOutDate());
            statement.setInt(6, transaction.getCustomer_id());
            statement.setString(7, transaction.getMedium());
            statement.setString(8, transaction.getTechnician());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> getTransactions() {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from transaction, customer, ecu where ecu_idecu = idecu and customer_idcustomer = idcustomer");
            return transactionRetriever.getList(statement, new TransactionBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Transaction> findTransactions(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from transaction, customer, ecu where ecu_idecu = idecu and customer_idcustomer = idcustomer and " +
                    "(technician like ? or name like ? or absID like ?)");
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");
            return transactionRetriever.getList(statement, new TransactionBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Transaction getTransactionById(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from transaction, customer, ecu where ecu_idecu = idecu and customer_idcustomer = idcustomer and idtransaction = ?");
            statement.setInt(1, id);
            List<Transaction> retrieved = transactionRetriever.getList(statement, new TransactionBuilder());
            if (retrieved.size() != 1)
                return null;
            return retrieved.get(0);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        try {
            String sql = "update transaction set " +
                    "ecu_idecu = ?," +
                    "indate = ?," +
                    "payment = ?," +
                    "description = ?," +
                    "outdate = ?," +
                    "customer_idcustomer = ?," +
                    "medium = ?," +
                    "technician = ? " +
                    "where idtransaction = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, transaction.getEcu_id());
            statement.setDate(2, transaction.getInDate());
            statement.setDouble(3, transaction.getPayment());
            statement.setString(4, transaction.getDescription());
            statement.setDate(5, transaction.getOutDate());
            statement.setInt(6, transaction.getCustomer_id());
            statement.setString(7, transaction.getMedium());
            statement.setString(8, transaction.getTechnician());
            statement.setInt(9, transaction.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AbsItem> getAvailableItems(int transactionId) {
        try {
            String query = "select item.*, package.name as packageName, type.name typeName from item, package, type " +
                    "where package_idpackage = idpackage and type_idtype=idtype and quantity > 0 and " +
                    "iditem not in (select item_iditem from transaction_has_item where transaction_idtransaction = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, transactionId);
            return itemRetriever.getList(statement, new ItemBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<AbsItem> getUsedItems(int transactionId) {
        try {
            PreparedStatement statement = connection.prepareStatement("select item.*, thi.quantity as used_quantity, package.name as packageName, type.name typeName " +
                    "from item, package, type, transaction_has_item thi " +
                    "where package_idpackage = idpackage and type_idtype=idtype and iditem = item_iditem and transaction_idtransaction = ?");
            statement.setInt(1, transactionId);
            return itemRetriever.getList(statement, new UsedItemBuilder());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void addItemToTransaction(int itemId, int transactionId) {
        try {
            String sql = "INSERT INTO transaction_has_item VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, transactionId);
            statement.setInt(2, itemId);
            statement.setInt(3, 1);

            statement.executeUpdate();

            updateQuantity(itemId, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeItemFromTransaction(int itemId, int transactionId) {
        try {
            String sql = "delete from transaction_has_item where transaction_idtransaction = ? and item_iditem = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, transactionId);
            statement.setInt(2, itemId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateItemUsage(int itemId, int transactionId, int quantity) {
        try {
            String sql = "update transaction_has_item set " +
                    "quantity = quantity + ? " +
                    "where transaction_idtransaction = ? and " +
                    "item_iditem = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, transactionId);
            statement.setInt(3, itemId);
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
                return new Customer(resultSet.getString("name"), resultSet.getString("phone_number"),
                        resultSet.getString("location"), resultSet.getInt("idcustomer"));
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
                        resultSet.getString("packageName"), resultSet.getString("typeName"),
                        resultSet.getInt("quantity"), resultSet.getDouble("cost"), resultSet.getDouble("selling_price"),
                        resultSet.getInt("package_idpackage"), resultSet.getInt("type_idtype"));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private static class UsedItemBuilder implements Function<ResultSet, AbsItem> {
        @Override
        public AbsItem apply(ResultSet resultSet) {
            try {
                return new AbsItem(resultSet.getInt("iditem"), resultSet.getString("name"),
                        resultSet.getString("packageName"), resultSet.getString("typeName"),
                        resultSet.getInt("used_quantity"), resultSet.getDouble("cost"), resultSet.getDouble("selling_price"),
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

    private static class TransactionBuilder implements Function<ResultSet, Transaction> {
        @Override
        public Transaction apply(ResultSet resultSet) {
            try {
                return new Transaction(resultSet.getInt("idtransaction"), resultSet.getInt("ecu_idecu"),
                        resultSet.getInt("customer_idcustomer"), resultSet.getDate("indate"),
                        resultSet.getDate("outdate"), resultSet.getDouble("payment"),
                        resultSet.getString("description"), resultSet.getString("medium"),
                        resultSet.getString("technician"), resultSet.getString("name"),
                        resultSet.getString("absID"));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
