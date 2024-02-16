import java.io.IOException;

import entity.User;
import javafx.fxml.FXML;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import util.HibernateMethods;

public class PrimaryController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField loginField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button signInBtn;

    @FXML
    void initialize() {

        signInBtn.setOnAction(event -> {
            try {
                String phoneNumber = phoneNumberField.getText();
                User user = getUserInfo(Long.parseLong(phoneNumber));
                if (user != null) {
                    HibbernateRunner.setRoot("mainpage", user);
                } else {
                    showErrorAlert("User not found", "User with the provided phone number was not found.");
                }
            } catch (NumberFormatException e) {
                showErrorAlert("Invalid phone number", "Please enter a valid phone number.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        signUpBtn.setOnAction(event -> {
            try {
                HibbernateRunner.setRoot("signUp", null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private User getUserInfo(long phoneNumber) {
        HibernateMethods hibernateMethods = new HibernateMethods();
        return hibernateMethods.getUserInfo(phoneNumber);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}