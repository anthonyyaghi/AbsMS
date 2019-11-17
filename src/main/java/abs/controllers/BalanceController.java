package abs.controllers;

import abs.db.DBImpl;
import abs.db.DBInterface;
import abs.dto.Transaction;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceController {
    private DBInterface db;

    public BalanceController() {
        db = new DBImpl();
    }

    @FXML
    private TextField customerNameField;

    @FXML
    private TableView table;

    @FXML
    private TextField ongoingField;

    @FXML
    private TextField pendingField;

    @FXML
    private TextField balanceField;

    @FXML
    public void initialize() {
        TableColumn<Integer, Transaction> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMinWidth(50);
        idCol.setResizable(false);

        TableColumn<String, Transaction> ecuCol = new TableColumn<>("ECU");
        ecuCol.setCellValueFactory(new PropertyValueFactory<>("ecu"));
        ecuCol.setMinWidth(90);
        ecuCol.setResizable(false);

        TableColumn<Date, Transaction> inDateCol = new TableColumn<>("in-Date");
        inDateCol.setCellValueFactory(new PropertyValueFactory<>("inDate"));
        inDateCol.setMinWidth(90);
        inDateCol.setResizable(false);

        TableColumn<Date, Transaction> outDateCol = new TableColumn<>("out-Date");
        outDateCol.setCellValueFactory(new PropertyValueFactory<>("outDate"));
        outDateCol.setMinWidth(90);
        outDateCol.setResizable(false);

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

        table.getColumns().addAll(idCol, ecuCol, inDateCol, outDateCol, paymentCol, techCol, payedCol);
    }

    @FXML
    void searchClicked(ActionEvent event) {
        listTransactions();

        ObservableList<Transaction> items = table.getItems();

        long ongoing = items.stream().filter(transaction -> transaction.getOutDate() == null).count();
        long pending = items.stream().filter(transaction -> !transaction.isPayed() && transaction.getOutDate() != null).count();
        double balance = items.stream().filter(transaction -> !transaction.isPayed() && transaction.getOutDate() != null).mapToDouble(Transaction::getPayment).sum();

        ongoingField.setText(String.valueOf(ongoing));
        pendingField.setText(String.valueOf(pending));
        balanceField.setText(String.valueOf(balance));
    }

//----------------------------------------------------------------------------------------------------------------------
    private void listTransactions() {
        table.getItems().clear();
        List<Transaction> transactions = db.getTransactions();
        table.getItems().addAll(transactions.stream().filter(transaction -> transaction.getCustomer().toLowerCase().
                contains(customerNameField.getText().trim().toLowerCase())).collect(Collectors.toList()));
    }
}
