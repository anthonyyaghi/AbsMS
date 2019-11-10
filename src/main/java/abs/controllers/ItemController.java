package abs.controllers;

import abs.controllers.utils.DigitFieldListener;
import abs.db.DBImpl;
import abs.db.DBInterface;
import abs.dto.AbsItem;
import abs.dto.ItemPackage;
import abs.dto.ItemType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ItemController {
    private DBInterface db;
    private AbsItem selectedItem;
    private int selectedIndex;

    public ItemController() {
        db = new DBImpl();
        selectedItem = null;
        selectedIndex = -1;
    }

    @FXML
    private TextField nameField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField costField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField packageField;

    @FXML
    private ComboBox<ItemType> typeBox;

    @FXML
    private ComboBox<ItemPackage> packageBox;

    @FXML
    private TextField searchField;

    @FXML
    private TableView table;

    @FXML
    private Label quantityLabel;

    @FXML
    private TextField quantityModField;

    @FXML
    private Label costLabel;

    @FXML
    private TextField costModField;

    @FXML
    private Label priceLabel;

    @FXML
    private TextField priceModField;


    @FXML
    public void initialize() {
        // force the field to be numeric only
        quantityField.textProperty().addListener(new DigitFieldListener(quantityField));
        costField.textProperty().addListener(new DigitFieldListener(costField));
        priceField.textProperty().addListener(new DigitFieldListener(priceField));
        quantityModField.textProperty().addListener(new DigitFieldListener(quantityModField));
        costModField.textProperty().addListener(new DigitFieldListener(costModField));
        priceModField.textProperty().addListener(new DigitFieldListener(priceModField));

        TableColumn<Integer, AbsItem> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMinWidth(100);
        idCol.setResizable(false);

        TableColumn<String, AbsItem> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(100);
        nameColumn.setResizable(false);

        TableColumn<String, AbsItem> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setMinWidth(100);
        quantityColumn.setResizable(false);


        table.getColumns().add(idCol);
        table.getColumns().add(nameColumn);
        table.getColumns().add(quantityColumn);
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedItem = (AbsItem) newValue;
            if (selectedItem == null) {
                selectedIndex = -1;
            } else {
                selectedIndex = table.getSelectionModel().getSelectedIndex();
            }
            refreshItemInfo();
        });

        listItems();
        listTypes();
        listPackages();
    }

    @FXML
    public void addItemClicked() {
        if (validItem()) {
            db.addItem(new AbsItem(-1, nameField.getText().trim(), Integer.parseInt(quantityField.getText()),
                    Double.parseDouble(costField.getText()), Double.parseDouble(priceField.getText()),
                    packageBox.getSelectionModel().getSelectedItem().getId(), typeBox.getSelectionModel().getSelectedItem().getId()));
            listItems();
            clearItemFields();
        } else {
            displayWarning("Missing item information.");
        }
    }

    @FXML
    public void addTypeClicked() {
        if (typeField.getText().trim().isEmpty()) {
            displayWarning("Missing type name");
            return;
        }
        db.addType(new ItemType(-1, typeField.getText().trim()));
        listTypes();
        typeField.clear();
    }

    @FXML
    public void addPackageClicked() {
        if (packageField.getText().trim().isEmpty()) {
            displayWarning("Missing package name");
            return;
        }
        db.addPackage(new ItemPackage(-1, packageField.getText().trim()));
        listPackages();
        packageField.clear();
    }

    @FXML
    public void searchBtnClicked() {
        table.getItems().clear();

        List<AbsItem> items = db.findItem(searchField.getText().trim());
        for (AbsItem item : items) {
            table.getItems().add(item);
        }
    }

    @FXML
    public void refreshBtnClicked() {
        listItems();
    }

    @FXML
    public void incClicked() {
        if (!validItemSelected()) {
            return;
        }
        if (quantityModField.getText().trim().isEmpty()) {
            return;
        }
        db.updateQuantity(selectedItem.getId(), Integer.parseInt(quantityModField.getText().trim()));
        quantityModField.clear();
        refreshTableItems();
    }

    @FXML
    public void subClicked() {
        if (!validItemSelected()) {
            return;
        }
        if (quantityModField.getText().trim().isEmpty()) {
            return;
        }
        db.updateQuantity(selectedItem.getId(), -Integer.parseInt(quantityModField.getText().trim()));
        quantityModField.clear();
        refreshTableItems();
    }

    @FXML
    public void setCostClicked() {
        if (!validItemSelected()) {
            return;
        }
        if (costModField.getText().trim().isEmpty()) {
            displayWarning("Enter a new cost.");
            return;
        }
        db.updateCost(selectedItem.getId(), Double.parseDouble(costModField.getText().trim()));
        costModField.clear();
        refreshTableItems();
    }

    @FXML
    public void setPriceClicked() {
        if (!validItemSelected()) {
            return;
        }
        if (priceModField.getText().trim().isEmpty()) {
            displayWarning("Enter a new price.");
            return;
        }
        db.updatePrice(selectedItem.getId(), Double.parseDouble(priceModField.getText().trim()));
        priceModField.clear();
        refreshTableItems();
    }

//    ------------------------------------------------------------------------------------------------------------------

    private void listItems() {
        table.getItems().clear();

        List<AbsItem> customers = db.getItems();
        for (AbsItem item : customers) {
            table.getItems().add(item);
        }
    }

    private void listTypes() {
        typeBox.getItems().clear();
        typeBox.getItems().addAll(db.getTypes());
    }

    private void listPackages() {
        packageBox.getItems().clear();
        packageBox.getItems().addAll(db.getPackages());
    }

    private boolean validItem() {
        if (nameField.getText().trim().isEmpty())
            return false;
        if (quantityField.getText().trim().isEmpty())
            return false;
        if (costField.getText().trim().isEmpty())
            return false;
        if (priceField.getText().trim().isEmpty())
            return false;
        if (typeBox.getSelectionModel().isEmpty())
            return false;
        if (packageBox.getSelectionModel().isEmpty())
            return false;
        return true;
    }

    private boolean validItemSelected() {
        if (selectedItem == null) {
            displayWarning("Select an item first.");
            return false;
        }
        return true;
    }

    private void clearItemFields() {
        nameField.clear();
        quantityField.clear();
        costField.clear();
        priceField.clear();
        typeBox.getSelectionModel().clearSelection();
        packageBox.getSelectionModel().clearSelection();
    }

    private void refreshItemInfo() {
        if (selectedItem != null) {
            quantityLabel.setText(String.valueOf(selectedItem.getQuantity()));
            costLabel.setText(String.valueOf(selectedItem.getCost()));
            priceLabel.setText(String.valueOf(selectedItem.getPrice()));
        }
        else {
            quantityLabel.setText("");
            costLabel.setText("");
            priceLabel.setText("");
        }
    }

    private void refreshTableItems() {
        int tempIndex = selectedIndex;
        listItems();
        table.requestFocus();
        table.getSelectionModel().select(tempIndex);
        table.getFocusModel().focus(tempIndex);
    }

    private void displayWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
