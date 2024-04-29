package HealthyDiaryApp.view;
/*
 * SettingsView class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас представляє відображення налаштувань користувача і наслідує клас BaseMenuClass.
Клас містить різні поля та методи для взаємодії з даними користувача та обробки подій кнопок.
 */
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import HealthyDiaryApp.HibbernateRunner;
import HealthyDiaryApp.controller.SettingsController;
import HealthyDiaryApp.enums.ActivityLevel;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.navigation.MouseEnterHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import HealthyDiaryApp.navigation.BaseMenuClass;

@Slf4j
public class SettingsView extends BaseMenuClass {
    private SettingsController settingsController = new SettingsController();
    @FXML
    private ImageView deleteImage;

    @FXML
    private Button deleteUserBtn;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField weightTextField;

    @FXML
    private ComboBox<ActivityLevel> activityLevelComboBox;

    @FXML
    private CheckBox causeCheckBox;

    @FXML
    private RadioButton manRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private Button updateDataBtn;

    @FXML
    private CheckBox allergyCheckBox;

    @FXML
    private VBox allergyVbox;

    @FXML
    private ImageView plusAllergyBtn;

    @FXML
    private AnchorPane deleteUserPane;

    @FXML
    private ImageView plusCancleImg;

    @FXML
    private Button cancelDeleteBtn;

    @FXML
    private Button deleteUserAccountBtn;

    @FXML
    private ImageView closeAppImg;

    @FXML
    private ImageView MinimizeAppImg;

    @FXML
    private Button createdMenuPageBtn;

    @FXML
    private Button loseWeightMenuButton;

    @FXML
    private Button changePageBtn;

    @FXML
    private Button progresisMenuItem;

    @FXML
    private Button forecastMenuItem;

    @FXML
    private Button calculatorPageBtn;

    @FXML
    private Button mainPageBtn;

    @FXML
    private ImageView maximizeAppImg;


    @FXML
    private ScrollPane scrollPane;

    @FXML
    void AddAllergy(MouseEvent event) {
        settingsController.addAllergy(allergyVbox, phoneNumberTextField, plusAllergyBtn, updateDataBtn);
    }

    @FXML
    void ClosePane(MouseEvent event) {
        settingsController.closePane(deleteUserPane);
    }

    // Метод для установки обработчиков событий наведения и убирания мыши с кнопки loseWeightMenuButton
    private void setMouseHandlers() {
        MouseEnterHandler.addMouseEnterHandler(loseWeightMenuButton, forecastMenuItem, progresisMenuItem, changePageBtn);
    }
    @FXML
    void initialize() {


        setMouseHandlers(); // Установить обработчики событий наведения и убирания мыши
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

        activityLevelComboBox.getItems().addAll(ActivityLevel.High, ActivityLevel.Medium, ActivityLevel.Low);

        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton, forecastMenuItem, progresisMenuItem);

        AnimationButton.addFadeAnimation(updateDataBtn);
        AnimationButton.addHoverAnimation(deleteUserBtn);

        AnimationButton.addHoverAnimation(cancelDeleteBtn);
        AnimationButton.addHoverAnimation(deleteUserAccountBtn);
        ToggleGroup group = new ToggleGroup();
        manRadioButton.setToggleGroup(group);
        femaleRadioButton.setToggleGroup(group);
        plusAllergyBtn.setOnMouseClicked(this::AddAllergy);
        User currentUser = settingsController.getUserFromApplicationContext();

        settingsController.initializeAllergies( allergyVbox,  updateDataBtn,  nameTextField,
                 ageTextField, phoneNumberTextField, heightTextField,
                 weightTextField, activityLevelComboBox, allergyCheckBox,
                manRadioButton, causeCheckBox, femaleRadioButton);

        // Обработчики событий для кнопок
        deleteUserBtn.setOnAction(event -> {
            deleteUserPane.setVisible(true);
            plusCancleImg.setOnMouseClicked(this::ClosePane);
        });

        updateDataBtn.setOnAction(event -> {
            settingsController.updateUserData(nameTextField, ageTextField, phoneNumberTextField,
                    heightTextField, weightTextField, activityLevelComboBox, allergyCheckBox,
                    manRadioButton);
        });

        maximizeAppImg.setOnMouseClicked(event -> {
            Stage stage = (Stage) maximizeAppImg.getScene().getWindow();
            if (stage.isFullScreen()) {
                stage.setFullScreen(false);
                stage.setWidth(1360); // Устанавливаем ширину окна
                stage.setHeight(720); // Устанавливаем высоту окна
            } else {
                stage.setFullScreen(true);
            }
        });


        cancelDeleteBtn.setOnAction(event -> deleteUserPane.setVisible(false));

        deleteUserAccountBtn.setOnAction(event -> {
            settingsController.deleteUserAccount(currentUser);
            try {
                HibbernateRunner.setRoot("primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}