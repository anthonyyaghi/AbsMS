package abs.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    public void ecuBtnClicked() {
        openNewWindow("ecu.fxml", "ECU Screen", 900, 600);
    }

    @FXML
    public void itemsBtnClicked() {
        openNewWindow("item.fxml", "Item Screen", 900, 600);
    }

    @FXML
    public void customersBtnClicked(){
        openNewWindow("customer.fxml", "Customer Screen", 900, 600);
    }

    @FXML
    public void transBtnClicked(){
        openNewWindow("transaction.fxml", "Transactions Screen", 900, 600);
    }

    @FXML
    public void balanceBtnClicked(){
        openNewWindow("balance.fxml", "Balance Screen", 640, 400);
    }

    private void openNewWindow(String path, String title, double width, double height) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(path));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
