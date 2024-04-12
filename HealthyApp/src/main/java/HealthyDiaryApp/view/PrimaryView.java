package HealthyDiaryApp.view;

import HealthyDiaryApp.navigation.BaseMenuClass;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;

/*
 * PrimaryView class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас представляє відображення основного вікна програми і реалізує інтерфейс Initializable для ініціалізації.
Клас містить різні поля та методи для взаємодії з елементами вікна та обробки подій кнопок.
 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import HealthyDiaryApp.controller.PrimaryController;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PrimaryView  implements Initializable {

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
    private ImageView closeAppImg;

    @FXML
    private ImageView MinimizeAppImg;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnimationButton.addFadeAnimation(signUpBtn);
        AnimationButton.addHoverAnimation(signInBtn);

        // Обработчик для закрытия приложения при нажатии на closeAppImg
        closeAppImg.setOnMouseClicked(event -> {
            // Получаем сцену и закрываем ее
            Stage stage = (Stage) closeAppImg.getScene().getWindow();
            stage.close();
        });

        // Обработчик для сворачивания окна при нажатии на MinimizeAppImg
        MinimizeAppImg.setOnMouseClicked(event -> {
            // Получаем сцену и минимизируем окно
            Stage stage = (Stage) MinimizeAppImg.getScene().getWindow();
            stage.setIconified(true);
        });

        signInBtn.setOnAction(event -> {
            if (loginField.getText().isEmpty() || phoneNumberField.getText().isEmpty()) {
                AnimationButton.moveButtonToLeftAndBack(signInBtn);
            }
            PrimaryController.handleSignIn(signInBtn, phoneNumberField);
        });

        signUpBtn.setOnAction(event -> {
            PrimaryController.handleSignUp(signUpBtn);
        });
    }
}