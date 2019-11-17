package abs.controllers;

import abs.controllers.utils.DigitFieldListener;
import abs.db.DBImpl;
import abs.db.DBInterface;
import abs.dto.AbsItem;
import abs.dto.Customer;
import abs.dto.Ecu;
import abs.dto.Transaction;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionController {
    private DBInterface db;

    private Transaction activeTransaction;
    private List<AbsItem> availableItems;

    public TransactionController() {
        db = new DBImpl();
        activeTransaction = null;
        availableItems = new ArrayList<>();
    }

    @FXML
    private CheckBox payedCheckBox;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<Customer> bCustomerBox;

    @FXML
    private ComboBox<Ecu> bEcuBox;

    @FXML
    private ComboBox<AbsItem> itemsBox;

    @FXML
    private TextField bMediumField;

    @FXML
    private TextField itemIdField;

    @FXML
    private DatePicker bDate;

    @FXML
    private TextArea bDescriptionArea;

    @FXML
    private TableView table;

    @FXML
    private TableView itemsTable;

    @FXML
    private TextField bTechField;

    @FXML
    private TextField eTransactionIdField;

    @FXML
    private ComboBox<Ecu> eEcuBox;

    @FXML
    private DatePicker eInDate;

    @FXML
    private DatePicker eOutDate;

    @FXML
    private TextField ePaymentField;

    @FXML
    private ComboBox<Customer> eCustomerBox;

    @FXML
    private TextField eMediumField;

    @FXML
    private TextField eTechField;

    @FXML
    private Label totalCostLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private TextArea eDescriptionArea;

    @FXML
    public void initialize() {
        eTransactionIdField.textProperty().addListener(new DigitFieldListener(eTransactionIdField));
        ePaymentField.textProperty().addListener(new DigitFieldListener(ePaymentField));
        itemIdField.textProperty().addListener(new DigitFieldListener(itemIdField));

        TableColumn<Integer, Transaction> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMinWidth(50);
        idCol.setResizable(false);

        TableColumn<String, Transaction> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        customerCol.setMinWidth(90);
        customerCol.setResizable(false);

        TableColumn<String, Transaction> ecuCol = new TableColumn<>("ECU");
        ecuCol.setCellValueFactory(new PropertyValueFactory<>("ecu"));
        ecuCol.setMinWidth(90);
        ecuCol.setResizable(false);

        TableColumn<Double, Transaction> paymentCol = new TableColumn<>("Payment");
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("payment"));
        paymentCol.setMinWidth(90);
        paymentCol.setResizable(false);

        TableColumn<String, Transaction> techCol = new TableColumn<>("M3alem");
        techCol.setCellValueFactory(new PropertyValueFactory<>("technician"));
        techCol.setMinWidth(90);
        techCol.setResizable(false);

        TableColumn<Boolean, Transaction> payedCol = new TableColumn<>("Payed");
        payedCol.setCellValueFactory(new PropertyValueFactory<>("payed"));
        payedCol.setMinWidth(90);
        payedCol.setResizable(false);

        table.getColumns().addAll(idCol, customerCol, ecuCol, paymentCol, techCol, payedCol);


        TableColumn<String, AbsItem> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(209);
        nameCol.setResizable(false);

        TableColumn<Integer, AbsItem> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setMinWidth(209);
        quantityCol.setResizable(false);

        itemsTable.getColumns().addAll(nameCol, quantityCol);


        listCustomers(bCustomerBox);
        listCustomers(eCustomerBox);
        listECUs(bEcuBox);
        listECUs(eEcuBox);
        listTransactions();
    }

    @FXML
    void createClicked(ActionEvent event) {
        if (bCustomerBox.getSelectionModel().isEmpty() || bEcuBox.getSelectionModel().isEmpty() ||
                bDate.getValue() == null || bTechField.getText().trim().isEmpty()) {
            displayWarning("Missing information.");
            return;
        }
        Ecu ecu = bEcuBox.getSelectionModel().getSelectedItem();
        Customer customer = bCustomerBox.getSelectionModel().getSelectedItem();
        java.sql.Date inDate = java.sql.Date.valueOf(bDate.getValue().toString());
        String description = bDescriptionArea.getText().trim();
        String medium = bMediumField.getText().trim();
        String technician = bTechField.getText().trim();

        db.addTransaction(new Transaction(-1, ecu.getEcuId(), customer.getId(), inDate, null, 0,
                description, medium, technician, customer.getName(), ecu.getAbsId(), false));

        clearTransactionFields();
        listTransactions();
    }

    @FXML
    void loadClicked() {
        if (eTransactionIdField.getText().trim().isEmpty()) {
            displayWarning("Enter an id");
            return;
        }
        activeTransaction = db.getTransactionById(Integer.parseInt(eTransactionIdField.getText().trim()));
        if (activeTransaction == null) {
            displayWarning("Transaction with specified id can't be loaded");
            clearEditFields();
            return;
        }
        availableItems = db.getAvailableItems(activeTransaction.getId());
        setEditFields();
    }

    @FXML
    void refreshClicked(ActionEvent event) {
        listTransactions();
    }

    @FXML
    void searchClicked(ActionEvent event) {
        table.getItems().clear();
        table.getItems().addAll(db.findTransactions(searchField.getText().trim()));
    }

    @FXML
    void updateClicked(ActionEvent event) {
        if (eTechField.getText().trim().isEmpty()) {
            displayWarning("Fish m3alem mstlema!");
            return;
        }

        int id = activeTransaction.getId();
        Ecu ecu = eEcuBox.getSelectionModel().getSelectedItem();
        Customer customer = eCustomerBox.getSelectionModel().getSelectedItem();
        java.sql.Date inDate = java.sql.Date.valueOf(eInDate.getValue().toString());

        java.sql.Date outDate;
        if (eOutDate.getValue() == null) {
             outDate = null;
        } else {
            outDate = java.sql.Date.valueOf(eOutDate.getValue());
        }

        double payment = 0.0d;
        if (!ePaymentField.getText().trim().isEmpty()) {
            payment = Double.parseDouble(ePaymentField.getText());
        }

        activeTransaction = new Transaction(id, ecu.getEcuId(), customer.getId(), inDate, outDate, payment,
                eDescriptionArea.getText().trim(), eMediumField.getText().trim(), eTechField.getText().trim(),
                customer.getName(), ecu.getAbsId(), payedCheckBox.isSelected());

        db.updateTransaction(activeTransaction);
        setEditFields();
    }

    @FXML
    void incItems(ActionEvent event) {
        if (itemsTable.getSelectionModel().isEmpty()) {
            displayWarning("Select an item first.");
            return;
        }
        AbsItem selectedItem = (AbsItem)(itemsTable.getSelectionModel().getSelectedItem());
        AbsItem matchingItem = db.getItemById(selectedItem.getId());

        if (matchingItem.getQuantity() < 1) {
            displayWarning("Not enough items in stock.");
            return;
        }

        db.updateItemUsage(selectedItem.getId(), activeTransaction.getId(), 1);
        db.updateQuantity(selectedItem.getId(), -1);
        listUsedItems();
    }

    @FXML
    void decItems(ActionEvent event) {
        if (itemsTable.getSelectionModel().isEmpty()) {
            displayWarning("Select an item first.");
            return;
        }

        AbsItem selectedItem = (AbsItem)(itemsTable.getSelectionModel().getSelectedItem());

        db.updateItemUsage(selectedItem.getId(), activeTransaction.getId(), -1);
        db.updateQuantity(selectedItem.getId(), +1);

        if (selectedItem.getQuantity() == 1) {
            db.removeItemFromTransaction(selectedItem.getId(), activeTransaction.getId());
        }

        listUsedItems();
    }

    @FXML
    void addItem(ActionEvent event) {
        if (itemsBox.getSelectionModel().isEmpty()) {
            displayWarning("Select an item first.");
            return;
        }
        db.addItemToTransaction(itemsBox.getSelectionModel().getSelectedItem().getId(), activeTransaction.getId());
        availableItems = db.getAvailableItems(activeTransaction.getId());
        setEditFields();
    }

    @FXML
    void addItemByID() {
        if (itemIdField.getText().trim().isEmpty()) {
            displayWarning("Enter an id first.");
            return;
        }

        AbsItem item = db.getItemById(Integer.parseInt(itemIdField.getText().trim()));
        if (item == null) {
            displayWarning("Item not found");
            return;
        }

        List<AbsItem> usedItems = itemsTable.getItems();
        for (AbsItem usedItem : usedItems) {
            if (usedItem.getId() == item.getId()) {
                displayWarning("Item already in transaction");
                return;
            }
        }

        if (item.getQuantity() < 1) {
            displayWarning("Item out of stock");
            return;
        }

        db.addItemToTransaction(item.getId(), activeTransaction.getId());
        availableItems = db.getAvailableItems(activeTransaction.getId());
        setEditFields();
    }

//    ------------------------------------------------------------------------------------------------------------------

    private void listCustomers(ComboBox<Customer> box) {
        box.getItems().clear();
        box.getItems().addAll(db.getCustomers());
    }

    private void listECUs(ComboBox<Ecu> box) {
        box.getItems().clear();
        box.getItems().addAll(db.getEcus());
    }

    private void listTransactions() {
        table.getItems().clear();
        table.getItems().addAll(db.getTransactions());
    }

    private void listUsedItems() {
        if (activeTransaction == null) {
            return;
        }
        itemsTable.getItems().clear();
        List<AbsItem> usedItems = db.getUsedItems(activeTransaction.getId());
        itemsTable.getItems().addAll(usedItems);
        totalCostLabel.setText(String.valueOf(usedItems.stream().mapToDouble(i -> i.getCost()*i.getQuantity()).sum()));
        totalPriceLabel.setText(String.valueOf(usedItems.stream().mapToDouble(i -> i.getPrice()*i.getQuantity()).sum()));
    }

    private void clearTransactionFields() {
        bCustomerBox.getSelectionModel().clearSelection();
        bEcuBox.getSelectionModel().clearSelection();
        bMediumField.clear();
        bDate.getEditor().clear();
        bTechField.clear();
        bDescriptionArea.clear();
    }

    private void clearEditFields() {
        eEcuBox.getSelectionModel().clearSelection();
        eInDate.getEditor().clear();
        eOutDate.getEditor().clear();
        ePaymentField.clear();
        eCustomerBox.getSelectionModel().clearSelection();
        eMediumField.clear();
        eTechField.clear();
        eDescriptionArea.clear();
        itemsTable.getItems().clear();
        itemsBox.getItems().clear();
        totalCostLabel.setText("");
        totalPriceLabel.setText("");
    }

    private void setEditFields() {
        if (activeTransaction != null) {
            eTransactionIdField.setText(String.valueOf(activeTransaction.getId()));
            eEcuBox.getSelectionModel().select(ecuDbToBoxId(activeTransaction.getEcu_id()));
            eCustomerBox.getSelectionModel().select(customerDbToBoxId(activeTransaction.getCustomer_id()));
            eInDate.setValue(activeTransaction.getInDate().toLocalDate());
            eTechField.setText(activeTransaction.getTechnician());
            ePaymentField.setText(String.valueOf(activeTransaction.getPayment()));

            if (activeTransaction.getOutDate() == null) {
                eOutDate.getEditor().clear();
            } else {
                eOutDate.setValue(activeTransaction.getOutDate().toLocalDate());
            }

            if (activeTransaction.getMedium() == null) {
                eMediumField.clear();
            } else {
                eMediumField.setText(activeTransaction.getMedium());
            }

            if (activeTransaction.getDescription() == null) {
                eDescriptionArea.clear();
            } else {
                eDescriptionArea.setText(activeTransaction.getDescription());
            }

            itemsBox.getItems().clear();
            itemsBox.getItems().addAll(availableItems);

            payedCheckBox.setSelected(activeTransaction.isPayed());

            listUsedItems();
        }
    }

    private int ecuDbToBoxId(int id) {
        ObservableList<Ecu> items = eEcuBox.getItems();
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i).getEcuId() == id)
                return i;
        }
        return -1;
    }

    private int customerDbToBoxId(int id) {
        ObservableList<Customer> items = eCustomerBox.getItems();
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i).getId() == id)
                return i;
        }
        return -1;
    }

    private void displayWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
