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


import HealthyDiaryApp.controller.ErrorDialogController;
import HealthyDiaryApp.navigation.BaseMenuClass;
import HealthyDiaryApp.util.UserComponent;
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
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SignUpView extends BaseMenuClass implements Initializable {
    private SignUpController signUpController = new SignUpController();
    private UserComponent userComponent = new UserComponent();
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

    @FXML
    private AnchorPane backPane;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView closeAppImg;

    @FXML
    private ImageView MinimizeAppImg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signUpActivityLevel.getItems().addAll(ActivityLevel.High, ActivityLevel.Medium, ActivityLevel.Low);
        signUpGender.getItems().addAll("чоловік", "жінка");

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

        nextBtn1.setOnAction(event -> {
            String phoneNumber = sighUpPhone.getText();
            if (!phoneNumber.isEmpty()) {
                long phone = Long.parseLong(phoneNumber);
                if (!userComponent.isPhoneNumberExists(phone)) {
                    signUpPane1.setVisible(false);
                    signUpPane2.setVisible(true);
                    Platform.runLater(() -> showExclamationMarks(signUpPane3));
                }else {
                    ErrorDialogController.showErrorAlert("Помилка","Цей номер телефона вже зайнятий");
                    return;
                }
            } else {
                // Обработка случая, когда поле с номером телефона пустое
            }

        });


        nextBtn2.setOnAction(event -> {
            if (isDataValid(signUpAge, signUpWeight, signUpHeight)) {
                signUpPane2.setVisible(false);
                signUpPane3.setVisible(true);
                Platform.runLater(() -> showExclamationMarks(signUpPane3));
            }
        });


        // Обработчик для backPane
        backPane.setOnMouseEntered(event -> {
            backPane.setOpacity(0.5); // Устанавливаем немного тусклый эффект при наведении
        });

        backPane.setOnMouseExited(event -> {
            backPane.setOpacity(1.0); // Возвращаем обычную непрозрачность после ухода мыши
        });

        initializeButtons(backBtn);


        // Добавляем слушатель для поля signUpName
        signUpName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (TextFieldValidator.isValidField(signUpName)) {
                signUpName.setStyle(""); // Убираем стиль при вводе правильных данных
            }
        });

        // Добавляем слушатель для поля sighUpPhone
        sighUpPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (TextFieldValidator.isValidField(sighUpPhone)) {
                sighUpPhone.setStyle(""); // Убираем стиль при вводе правильных данных
            }
        });

        // Добавляем слушатель для поля signUpAge
        signUpAge.textProperty().addListener((observable, oldValue, newValue) -> {
            if (TextFieldValidator.isValidField(signUpAge)) {
                signUpAge.setStyle(""); // Убираем стиль при вводе правильных данных
            }
        });

        // Добавляем слушатель для поля signUpWeight
        signUpWeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (TextFieldValidator.isValidField(signUpWeight)) {
                signUpWeight.setStyle(""); // Убираем стиль при вводе правильных данных
            }
        });

        // Добавляем слушатель для поля signUpHeight
        signUpHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (TextFieldValidator.isValidField(signUpHeight)) {
                signUpHeight.setStyle(""); // Убираем стиль при вводе правильных данных
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
            if (!TextFieldValidator.isValidField(field)) {
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