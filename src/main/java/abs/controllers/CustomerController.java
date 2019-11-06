package abs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import abs.db.DBImpl;
import abs.db.DBInterface;
import abs.dto.Customer;

import java.util.List;

public class CustomerController {
    private DBInterface db;

    public CustomerController() {
        db = new DBImpl();
    }
    @FXML
    TableView table;
    @FXML
    TextField nameField;
    @FXML
    TextField phoneField;
    @FXML
    TextField addressField;
    @FXML
    TextField searchField;

    @FXML
    public void initialize() {
        TableColumn<String, Customer> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(200);
        nameColumn.setResizable(false);

        TableColumn<String, Customer> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setMinWidth(200);
        phoneColumn.setResizable(false);

        TableColumn<String, Customer> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setMinWidth(200);
        addressColumn.setResizable(false);


        table.getColumns().add(nameColumn);
        table.getColumns().add(phoneColumn);
        table.getColumns().add(addressColumn);

        listAllCustomers();
    }

    @FXML
    public void refreshTable() {
        listAllCustomers();
    }

    @FXML
    public void registerBtnClicked() {
        db.addCustomer(new Customer(nameField.getText().trim(), phoneField.getText().trim(), addressField.getText().trim()));
        nameField.setText("");
        phoneField.setText("");
        addressField.setText("");
        listAllCustomers();
    }

    @FXML
    public void searchBtnClicked() {
        table.getItems().clear();

        List<Customer> customers = db.findCustomer(searchField.getText().trim());
        for (Customer customer : customers) {
            table.getItems().add(customer);
        }
    }

    private void listAllCustomers() {
        table.getItems().clear();

        List<Customer> customers = db.getCustomers();
        for (Customer customer : customers) {
            table.getItems().add(customer);
        }
    }
}
