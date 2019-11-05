package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.db.DBImpl;
import sample.db.DBInterface;
import sample.dto.Customer;

import java.util.List;

public class ItemController {
    private DBInterface db;

    public ItemController() {
        db = new DBImpl();
    }

    @FXML
    TableView table;
    @FXML
    TextField nameField;
    @FXML
    TextField searchField;

    @FXML
    public void initialize() {
        TableColumn<String, Customer> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(200);

        TableColumn<String, Customer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setMinWidth(200);


        table.getColumns().add(nameColumn);
        table.getColumns().add(quantityColumn);

        listAllItems();
    }

    @FXML
    public void addItemClicked() {

    }

    @FXML
    public void addTypeClicked() {

    }@FXML
    public void addPackageClicked() {

    }

    @FXML
    public void searchBtnClicked() {

    }

    @FXML
    public void refreshTable() {
        listAllItems();
    }

    @FXML
    public void incClicked() {

    }

    @FXML
    public void subClicked() {

    }

    @FXML
    public void setCostClicked() {

    }

    @FXML
    public void setPriceClicked() {

    }

//    ----------------------------------------------------------------

    private void listAllItems() {
        table.getItems().clear();

        List<Customer> customers = db.listAllCustomers();
        for (Customer customer : customers) {
            table.getItems().add(customer);
        }
    }
}
