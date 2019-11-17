package abs.dto;


import java.sql.Date;

public class Transaction {
    private int id, ecu_id, customer_id;
    private Date inDate, outDate;
    private double payment;
    private String description, medium, technician, customer, ecu;
    private boolean payed;

    public Transaction(int id, int ecu_id, int customer_id, Date inDate, Date outDate, double payment, String description,
                       String medium, String technician, String customer, String ecu, boolean payed) {
        this.id = id;
        this.ecu_id = ecu_id;
        this.customer_id = customer_id;
        this.inDate = inDate;
        this.outDate = outDate;
        this.payment = payment;
        this.description = description;
        this.medium = medium;
        this.technician = technician;
        this.customer = customer;
        this.ecu = ecu;
        this.payed = payed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEcu_id() {
        return ecu_id;
    }

    public void setEcu_id(int ecu_id) {
        this.ecu_id = ecu_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEcu() {
        return ecu;
    }

    public void setEcu(String ecu) {
        this.ecu = ecu;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }
}
