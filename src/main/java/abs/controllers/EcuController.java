package abs.controllers;

import abs.db.DBImpl;
import abs.db.DBInterface;
import abs.dto.Ecu;
import abs.dto.EcuType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EcuController {
    private DBInterface db;

    @FXML
    private TextField idField;

    @FXML
    private TextField typeField;

    @FXML
    private ComboBox<EcuType> typeBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button registerBtn;

    @FXML
    private TextField searchField;

    @FXML
    private TableView table;

    public EcuController() {
        db = new DBImpl();
    }

    @FXML
    public void initialize() {
        listEcuTypes();
        listEcus();

        TableColumn<String, Ecu> idColum = new TableColumn<>("ID");
        idColum.setCellValueFactory(new PropertyValueFactory<>("absId"));
        idColum.setMinWidth(100);
        idColum.setResizable(false);

        TableColumn<String, Ecu> descColumn = new TableColumn<>("Description");
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descColumn.setMinWidth(200);
        descColumn.setResizable(false);

        table.getColumns().add(idColum);
        table.getColumns().add(descColumn);
    }

    @FXML
    void addClicked(ActionEvent event) {
        db.addEcu(new Ecu(-1, typeBox.getSelectionModel().getSelectedItem().getId(), descriptionArea.getText(),
                idField.getText().trim()));
        listEcus();
    }

    @FXML
    void generateClicked(ActionEvent event) {
        if (typeBox.getSelectionModel().isEmpty()) {
            displayWarning("Select ecu type.");
        } else {
            StringBuilder id = new StringBuilder();
            id.append(typeBox.getSelectionModel().getSelectedItem().getName().toUpperCase().charAt(0));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            id.append(now.getYear() % 2000);
            id.append(now.getMonthValue());
            int num = db.getNextEcuNumber(id.toString());
            if (num == -1) {
                displayWarning("Something went wrong.");
                return;
            }
            id.append(now.getDayOfMonth());
            id.append(num);
            idField.setText(id.toString());
        }
    }

    @FXML
    void refreshClicked(ActionEvent event) {
        listEcus();
    }

    @FXML
    void searchClicked(ActionEvent event) {

    }

    @FXML
    void addTypeClicked(ActionEvent event) {
        if (typeField.getText().trim().isEmpty()) {
            displayWarning("Enter a type name");
            return;
        }
        db.addEcuType(new EcuType(-1, typeField.getText().trim()));
        typeField.clear();
        listEcuTypes();
    }

    private void displayWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void listEcuTypes() {
        typeBox.getItems().clear();
        typeBox.getItems().addAll(db.getEcuTypes());
    }

    private void listEcus() {
        table.getItems().clear();
        List<Ecu> ecus = db.getEcus();
        table.getItems().addAll(ecus);
    }
}
