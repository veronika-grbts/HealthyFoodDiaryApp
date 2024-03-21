package HealthyDiaryApp.view;
/*
 * SignUpView class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас представляє відображення реєстрації користувача, реалізує інтерфейс Initializable для ініціалізації контролера.
Клас містить різні поля та методи для обробки даних користувача та відображення відповідних повідомлень про помилки.
 */

import java.net.URL;
import java.util.*;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import HealthyDiaryApp.controller.SignUpController;
import HealthyDiaryApp.enums.ActivityLevel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SignUpView implements Initializable {
    private SignUpController signUpController = new SignUpController();
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane yourImageView;

    @FXML
    private AnchorPane imagePlus;

    @FXML
    private AnchorPane signUpPane1;

    @FXML
    private TextField signUpName;

    @FXML
    private TextField sighUpPhone;

    @FXML
    private Button nextBtn1;

    @FXML
    private AnchorPane signUpPane2;

    @FXML
    private TextField signUpAge;

    @FXML
    private TextField signUpWeight;

    @FXML
    private TextField signUpHeight;

    @FXML
    private ComboBox<ActivityLevel> signUpActivityLevel;

    @FXML
    private ComboBox<String> signUpGender;

    @FXML
    private Button nextBtn2;

    @FXML
    private AnchorPane signUpPane3;

    @FXML
    private CheckBox checkBoxAllergy;

    @FXML
    private CheckBox checkBoxCause;

    @FXML
    private Button finishBtn;

    @FXML
    private ImageView ImagePlus;

    @FXML
    private ImageView exclamationPointImg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signUpActivityLevel.getItems().addAll(ActivityLevel.High, ActivityLevel.Medium, ActivityLevel.Low);
        signUpGender.getItems().addAll("чоловік", "жінка");

        nextBtn1.setOnAction(event -> {
            if (isDataValid(signUpName, sighUpPhone)) {
                signUpPane1.setVisible(false);
                signUpPane2.setVisible(true);

                // Показать восклицательные знаки после установки видимости signUpPane2
                Platform.runLater(() -> showExclamationMarks(signUpPane3));
            }
        });

        nextBtn2.setOnAction(event -> {
            if (isDataValid(signUpAge, signUpWeight, signUpHeight)) {
                signUpPane2.setVisible(false);
                signUpPane3.setVisible(true);
                // Показать восклицательные знаки после установки видимости signUpPane3
                Platform.runLater(() -> showExclamationMarks(signUpPane3));
            }
        });
        ImagePlus.setOnMouseClicked(event -> signUpController.handleImagePlusClicked(sighUpPhone, signUpPane3));
        AnimationButton.addHoverAnimation(finishBtn);
        finishBtn.setOnAction(event -> {
            handleFinishBtn(event);
        });
    }

    @FXML
    void handleFinishBtn(ActionEvent event) {
        signUpController.handleFinishBtn( finishBtn,  signUpName,  sighUpPhone,  signUpAge,
                 signUpWeight,  signUpHeight, signUpGender,
                 signUpActivityLevel,  checkBoxAllergy,
                 checkBoxCause,  signUpPane3);
    }

    @FXML
    private boolean isDataValid(TextField... fields) {
        for (TextField field : fields) {
            if (!isValidField(field)) {
                showExclamationMarkForField(field);
                return false;
            }
        }
        return true;
    }

    @FXML
    private boolean isValidField(TextField field) {
        if (field == null || field.getText().trim().isEmpty()) {
            return false;
        }

        switch (field.getId()) {
            case "sighUpPhone":
                return isPhoneValid(field.getText());
            case "signUpAge":
                return isAgeValid(field.getText());
            case "signUpWeight":
                return isWeightValid(field.getText());
            case "signUpHeight":
                return isHeightValid(field.getText());
            default:
                return true;
        }
    }

    @FXML
    private void showExclamationMarks(AnchorPane pane) {
        for (javafx.scene.Node node : pane.getChildren()) {
            if (node instanceof TextField) {
                TextField field = (TextField) node;
                if (!isValidField(field)) {
                    showExclamationMarkForField(field);
                }
            }
        }
    }

    @FXML
    private void showExclamationMarkForField(TextField field) {
        exclamationPointImg.setVisible(true);
        double fieldX = field.getLayoutX();
        double fieldY = field.getLayoutY();
        double fieldWidth = field.getWidth();

        exclamationPointImg.setLayoutX(fieldX + fieldWidth + 5);
        exclamationPointImg.setLayoutY(fieldY);
    }

    @FXML
    private boolean isPhoneValid(String phone) {
        return phone.matches("^380\\d{9}$");
    }

    @FXML
    private boolean isAgeValid(String age) {
        return age.matches("\\d+");
    }

    @FXML
    private boolean isHeightValid(String height) {
        return height.matches("\\d+\\.?\\d*");
    }

    @FXML
    private boolean isWeightValid(String weight) {
        return weight.matches("\\d+(\\.\\d+)?");
    }

    @FXML
    private void handleImagePlusClicked(MouseEvent event) {
        signUpPane2.setVisible(false);
        signUpPane3.setVisible(true);
    }

    @FXML
    private void handleImagePlusClicked(ActionEvent event) {
        signUpPane2.setVisible(false);
        signUpPane3.setVisible(true);
        handleImagePlusClicked(event);
    }



}