package abs.dto;

public class AbsItem {
    private int id;
    private String name, packageName, typeName;
    private int quantity;
    private double cost, price;
    private int packageId, typeId;

    public AbsItem(int id, String name, String packageName, String typeName, int quantity, double cost, double price, int packageId, int typeId) {
        this.id = id;
        this.name = name;
        this.packageName = packageName;
        this.typeName = typeName;
        this.quantity = quantity;
        this.cost = cost;
        this.price = price;
        this.packageId = packageId;
        this.typeId = typeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return name + "(id: " + id + ")";
    }
}
