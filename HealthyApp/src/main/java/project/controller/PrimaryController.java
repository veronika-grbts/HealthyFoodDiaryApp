package project.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import project.HibbernateRunner;
import project.entity.User;
import project.method.NavigationMenu;
import project.singleton.ApplicationContext;
import project.util.HibernateMethods;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryController {
    public static void handleSignIn(Button signInBtn, TextField phoneNumberField) {
        signInBtn.setOnAction(event -> {
            try {
                String phoneNumber = phoneNumberField.getText();
                User user = getUserInfo(Long.parseLong(phoneNumber));
                if (user != null) {
                    // Зберігаємо user в ApplicationContext
                    ApplicationContext.getInstance().setCurrentUser(user);
                    HibbernateRunner.setRoot("mainpage");
                } else {
                    showErrorAlert("User not found", "User with the provided phone number was not found.");
                }
            } catch (NumberFormatException e) {
                showErrorAlert("Invalid phone number", "Please enter a valid phone number.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }

    public static void handleSignUp(Button signUpBtn) {
        signUpBtn.setOnAction(event -> {
            NavigationMenu.navigateToPage("signUp");
        });
    }

    private static User getUserInfo(long phoneNumber) {
        HibernateMethods hibernateMethods = new HibernateMethods();
        return hibernateMethods.getUserInfo(phoneNumber);
    }

    private static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

