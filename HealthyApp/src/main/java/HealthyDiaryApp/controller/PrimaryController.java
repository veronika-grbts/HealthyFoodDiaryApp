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

import HealthyDiaryApp.view.ErrorDialogView;
import HealthyDiaryApp.view.PrimaryView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import HealthyDiaryApp.HibbernateRunner;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.navigation.NavigationMenu;
import HealthyDiaryApp.singleton.ApplicationContext;
import HealthyDiaryApp.util.UserComponent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class PrimaryController {

    public static void handleSignIn(Stage stage ,Button signInBtn, TextField phoneNumberField) {
        try {
            String phoneNumber = phoneNumberField.getText();
            User user = getUserInfo(Long.parseLong(phoneNumber));
            if (user != null) {
                // Зберігаємо user в ApplicationContext
                ApplicationContext.getInstance().setCurrentUser(user);

                HibbernateRunner.setRoot(stage,"mainpage");
            } else {
                ErrorDialogController.showErrorAlert("Користувач не знайдений", "Користувач із вказаним номером телефону не знайдений.");
            }
        } catch (NumberFormatException e) {
            ErrorDialogController.showErrorAlert("Недійсний номер телефону", "Будь ласка введіть дійсний номер телефону та ім'я користувача.");
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

}


