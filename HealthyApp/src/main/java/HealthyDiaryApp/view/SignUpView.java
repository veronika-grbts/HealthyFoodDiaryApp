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
import javafx.scene.control.*;
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

    @FXML
    private Stage stage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signUpActivityLevel.getItems().addAll(ActivityLevel.High, ActivityLevel.Medium, ActivityLevel.Low);
        signUpGender.getItems().addAll("чоловік", "жінка");

        closeAppImg.setOnMouseClicked(event -> {
            Stage stage = (Stage) closeAppImg.getScene().getWindow();
            stage.close();
        });

        MinimizeAppImg.setOnMouseClicked(event -> {
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
                    Platform.runLater(() -> showExclamationMarks(signUpPane2));
                } else {
                    ErrorDialogController.showErrorAlert("Помилка","Цей номер телефона вже зайнятий");
                    return;
                }
            } else {
                ErrorDialogController.showErrorAlert("Помилка","Будь ласка, введіть номер телефону");
                return;
            }
        });

        nextBtn2.setOnAction(event -> {
            if (isDataValid(signUpAge, signUpWeight, signUpHeight) && isComboBoxValid(signUpGender) && isComboBoxValid(signUpActivityLevel)) {
                signUpPane2.setVisible(false);
                signUpPane3.setVisible(true);
                Platform.runLater(() -> showExclamationMarks(signUpPane3));
            }
        });

        initializeButtons(backBtn);


        signUpName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (TextFieldValidator.isValidField(signUpName)) {
                signUpName.setStyle("");
            }
        });

        sighUpPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (TextFieldValidator.isValidField(sighUpPhone)) {
                sighUpPhone.setStyle("");
            }
        });

        signUpAge.textProperty().addListener((observable, oldValue, newValue) -> {
            if (TextFieldValidator.isValidField(signUpAge)) {
                signUpAge.setStyle("");
            }
        });

        signUpWeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (TextFieldValidator.isValidField(signUpWeight)) {
                signUpWeight.setStyle("");
            }
        });

        signUpHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (TextFieldValidator.isValidField(signUpHeight)) {
                signUpHeight.setStyle("");
            }
        });

        ImagePlus.setOnMouseClicked(event -> signUpController.handleImagePlusClicked(sighUpPhone, signUpPane3));
        AnimationButton.addHoverAnimation(finishBtn);
        AnimationButton.addHoverAnimation(nextBtn1);
        AnimationButton.addHoverAnimation(nextBtn2);
        finishBtn.setOnAction(event -> {
            handleFinishBtn(event);
        });
    }


    @FXML
    void handleFinishBtn(ActionEvent event) {
        // Присваиваем значение stage в методе initialize
        this.stage = (Stage) backBtn.getScene().getWindow();

        signUpController.handleFinishBtn(stage,finishBtn, signUpName, sighUpPhone, signUpAge,
                signUpWeight, signUpHeight, signUpGender,
                signUpActivityLevel, checkBoxAllergy,
                checkBoxCause, signUpPane3);
    }

    private boolean isDataValid(TextField... fields) {
        for (TextField field : fields) {
            if (!TextFieldValidator.isValidField(field)) {
                return false;
            }
        }
        return true;
    }

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

    private <T> boolean isComboBoxValid(ComboBox<T>... comboBoxes) {
        for (ComboBox<T> comboBox : comboBoxes) {
            if (comboBox.getValue() == null) {
                return false;
            }
        }
        return true;
    }

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

    private void showExclamationMarkForField(TextField field) {
        exclamationPointImg.setVisible(true);
        double fieldX = field.getLayoutX();
        double fieldY = field.getLayoutY();
        double fieldWidth = field.getWidth();

        exclamationPointImg.setLayoutX(fieldX + fieldWidth + 5);
        exclamationPointImg.setLayoutY(fieldY);
    }

    private boolean isPhoneValid(String phone) {
        return phone.matches("^380\\d{9}$");
    }

    private boolean isAgeValid(String age) {
        return age.matches("\\d+");
    }

    private boolean isHeightValid(String height) {
        return height.matches("\\d+\\.?\\d*");
    }

    private boolean isWeightValid(String weight) {
        return weight.matches("\\d+(\\.\\d+)?");
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    @FXML
    private void handleImagePlusClicked(ActionEvent event) {
        signUpPane2.setVisible(false);
        signUpPane3.setVisible(true);
        handleImagePlusClicked(event);
    }

}