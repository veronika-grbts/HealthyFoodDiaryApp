/*
 * PrimaryController  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Контролер, що відповідає за обробку подій під час авторизації користувача.
 */
package HealthyDiaryApp.controller;

import javafx.scene.control.*;
import HealthyDiaryApp.HibbernateRunner;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.navigation.NavigationMenu;
import HealthyDiaryApp.singleton.ApplicationContext;
import HealthyDiaryApp.util.UserComponent;

import java.io.IOException;

public class PrimaryController {
    public static void handleSignIn(Button signInBtn, TextField phoneNumberField) {
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
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }

    public static void handleSignUp(Button signUpBtn) {
        NavigationMenu.navigateToPage("signUp");
    }

    private static User getUserInfo(long phoneNumber) {
        UserComponent userComponent = new UserComponent();
        return userComponent.getUserInfo(phoneNumber);
    }

    private static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

