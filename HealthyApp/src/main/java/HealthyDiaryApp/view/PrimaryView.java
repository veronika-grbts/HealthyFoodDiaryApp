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

        @Override
        public void initialize (URL location, ResourceBundle resources){


            normalWidth = 1076;
            normalHeight = 720;

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

            maximizeAppImg.setOnMouseClicked(event -> {
                Stage stage = (Stage) maximizeAppImg.getScene().getWindow();
                if (stage.isFullScreen()) {
                    // Возвращаем окно в обычный режим
                    stage.setFullScreen(false);
                    stage.setWidth(1360);
                    stage.setHeight(720);
                    // Возвращаем изображение к исходным размерам
                    normalWidth = 1052;
                    normalHeight = 720;
                    mainPageImg.setFitWidth(normalWidth);
                    mainPageImg.setFitHeight(normalHeight);
                    mainPageImg.setPreserveRatio(true); // Сохраняем пропорции изображения

                    // Сохраняем размеры и положение окна перед изменением
                    WindowSize.width = 1360;
                    WindowSize.height = 720;
                    WindowSize.x = stage.getX();
                    WindowSize.y = stage.getY();
                } else {
                    // Устанавливаем окно в полноэкранный режим
                    stage.setFullScreen(true);
                    // Сохраняем текущие размеры окна
                    normalWidth = stage.getWidth();
                    normalHeight = stage.getHeight();
                    // Устанавливаем размеры изображения равными размерам окна
                    mainPageImg.setFitWidth(normalWidth);
                    mainPageImg.setFitHeight(normalHeight);
                    mainPageImg.setPreserveRatio(true); // Сохраняем пропорции изображения

                    // Сохраняем размеры и положение окна перед изменением
                    WindowSize.width = stage.getWidth();
                    WindowSize.height = stage.getHeight();
                    WindowSize.x = stage.getX();
                    WindowSize.y = stage.getY();
                }
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
