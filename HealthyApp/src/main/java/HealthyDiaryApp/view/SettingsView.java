package HealthyDiaryApp.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import HealthyDiaryApp.HibbernateRunner;
import HealthyDiaryApp.controller.SettingsController;
import HealthyDiaryApp.enums.ActivityLevel;
import HealthyDiaryApp.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import HealthyDiaryApp.navigation.BaseMenuClass;

@Slf4j
public class SettingsView extends BaseMenuClass {
    private SettingsController settingsController = new SettingsController();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button deleteUserBtn;

    @FXML
    private ImageView deleteImage;

    @FXML
    private Button mainPageBtn;

    @FXML
    private Button calculatorPageBtn;

    @FXML
    private Button createdMenuPageBtn;

    @FXML
    private Button changePageBtn;

    @FXML
    private SplitMenuButton loseWeightMenuButton;

    @FXML
    private MenuItem forecastMenuItem;

    @FXML
    private MenuItem statisticsMenuItem;

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
    void AddAllergy(MouseEvent event) {
        settingsController.addAllergy(allergyVbox, phoneNumberTextField, plusAllergyBtn, updateDataBtn);
    }

    @FXML
    void ClosePane(MouseEvent event) {
        settingsController.closePane(deleteUserPane);
    }

    @FXML
    void initialize() {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn);
        initializeMenuButton(loseWeightMenuButton);
        initializeMenuItem(forecastMenuItem);

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