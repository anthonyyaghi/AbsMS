package abs.controllers;

import abs.db.ConnectionProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectController {
    @FXML
    private TextField hostField;

    @FXML
    private TextField dbField;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    @FXML
    public void initialize() {
        File f = new File("params.abs");
        if (!f.exists())
            return;
        try {
            FileInputStream fi = new FileInputStream(new File("params.abs"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            ArrayList<String> params = (ArrayList<String>) oi.readObject();
            hostField.setText(params.get(0));
            dbField.setText(params.get(1));
            userField.setText(params.get(2));
            passField.setText(params.get(3));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void connectClicked(ActionEvent event) {
        if (hostField.getText().trim().isEmpty() || dbField.getText().trim().isEmpty() ||
                userField.getText().trim().isEmpty() || passField.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Missing information.");
            alert.showAndWait();
        } else {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://" + hostField.getText().trim() +
                        "/" + dbField.getText().trim(), userField.getText().trim(), passField.getText().trim());
                ConnectionProvider.setConnection(connection);
                openNewWindow("menu.fxml", "Customer Menu", 810, 200);
                saveParams();
                Stage stage = (Stage) hostField.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection error");
                alert.setHeaderText(null);
                alert.setContentText("Can not connect to the database.");
                alert.showAndWait();
            }
        }
    }

    private void saveParams() {
        try {
            ArrayList<String> params = new ArrayList<>();
            params.clear();
            params.add(hostField.getText());
            params.add(dbField.getText());
            params.add(userField.getText());
            params.add(passField.getText());

            FileOutputStream f = new FileOutputStream(new File("params.abs"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(params);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
