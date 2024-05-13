package HealthyDiaryApp.view;

import HealthyDiaryApp.navigation.BaseMenuClass;
import com.sun.glass.ui.Application;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import HealthyDiaryApp.controller.PrimaryController;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PrimaryView  implements Initializable  {


    @FXML
    private ImageView mainPageImg;

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

    @FXML
    private ImageView maximizeAppImg;

    @FXML
    private AnchorPane topBtnMenu;
    private double normalWidth;
    private double normalHeight;

    private Stage stage;

        @Override
        public void initialize (URL location, ResourceBundle resources){
            normalWidth = 1076;
            normalHeight = 720;

            AnimationButton.addFadeAnimation(signUpBtn);
            AnimationButton.addHoverAnimation(signInBtn);

            // Привязка обработчиков событий к кнопкам
            closeAppImg.setOnMouseClicked(event -> {
                Stage primaryStage = (Stage) closeAppImg.getScene().getWindow();
                primaryStage.close();
            });

            MinimizeAppImg.setOnMouseClicked(event -> {
                Stage primaryStage = (Stage) MinimizeAppImg.getScene().getWindow();
                primaryStage.setIconified(true);
            });

            maximizeAppImg.setOnMouseClicked(event -> {
                Stage primaryStage = (Stage) maximizeAppImg.getScene().getWindow();
                if (primaryStage.isFullScreen()) {
                    primaryStage.setFullScreen(false);
                    primaryStage.setWidth(1360);
                    primaryStage.setHeight(720);
                    // Сохраняем новые размеры окна
                    WindowSize.saveNewWindowSize(1360, 720);
                } else {
                    primaryStage.setFullScreen(true);
                    // Сохраняем новые размеры окна
                    WindowSize.saveNewWindowSize(primaryStage.getWidth(), primaryStage.getHeight());
                }
            });

            signInBtn.setOnAction(event -> {
                // Получаем текущую сцену
                Scene scene = mainPageImg.getScene();
                // Получаем Stage из сцены
                Stage stage = (Stage) scene.getWindow();

                if (loginField.getText().isEmpty() || phoneNumberField.getText().isEmpty()) {
                    AnimationButton.moveButtonToLeftAndBack(signInBtn);
                }
                PrimaryController.handleSignIn(stage,signInBtn, phoneNumberField);
            });

            signUpBtn.setOnAction(event -> {
                PrimaryController.handleSignUp(signUpBtn);
            });
        }

}
