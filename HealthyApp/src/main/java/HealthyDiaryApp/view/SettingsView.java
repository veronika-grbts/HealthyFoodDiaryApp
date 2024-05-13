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
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
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
    private AnchorPane deleteUserPane;

    @FXML
    private ImageView plusCancleImg;

    @FXML
    private Button cancelDeleteBtn;

    @FXML
    private Button deleteUserAccountBtn;

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
    private ImageView closeMenu;

    @FXML
    private AnchorPane topBtnMenu;

    @FXML
    private ImageView maximizeAppImg;

    @FXML
    private ImageView closeAppImg;

    @FXML
    private ImageView MinimizeAppImg;

    @FXML
    private ScrollPane myScrollPane;

    @FXML
    private AnchorPane myAnchorePane;

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
    private VBox VBoxMenu;

    @FXML
    private ImageView openMenu;

    @FXML
    private Pane deleteBtnPane;

    @FXML
    private Label namePage;

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


    // Метод для анимации закрытия меню
    private void slideOutMenu() {
        // Анимация исчезновения VBoxMenu
        Transition transition = new Transition() {
            {
                setCycleDuration(Duration.seconds(0.5));
            }

            @Override
            protected void interpolate(double frac) {
                VBoxMenu.setTranslateX(-VBoxMenu.getWidth() * frac);
            }
        };
        transition.play();

        // Анимация перемещения paneWithInfo и PaneName
        TranslateTransition infoTransition = new TranslateTransition(Duration.seconds(0.5), myAnchorePane);
        infoTransition.setToX(-VBoxMenu.getWidth()); // Перемещаем paneWithInfo влево на ширину меню
        infoTransition.play();

        TranslateTransition nameTransition = new TranslateTransition(Duration.seconds(0.5), namePage);
        nameTransition.setToX(-VBoxMenu.getWidth()); // Перемещаем PaneName влево на ширину меню
        nameTransition.play();


        TranslateTransition btnTransition = new TranslateTransition(Duration.seconds(0.5), deleteUserBtn);
        btnTransition.setToX(-VBoxMenu.getWidth()); // Перемещаем PaneName влево на ширину меню
        btnTransition.play();

        TranslateTransition btnImgTransition = new TranslateTransition(Duration.seconds(0.5), deleteImage);
        btnImgTransition.setToX(-VBoxMenu.getWidth()); // Перемещаем PaneName влево на ширину меню
        btnImgTransition.play();

        // Показываем openMenu
        openMenu.setVisible(true);

        // Включаем анимацию появления кнопки openMenu
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), openMenu);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    // Метод для анимации открытия меню
    private void slideInMenu() {
        // Анимация появления VBoxMenu
        Transition transition = new Transition() {
            {
                setCycleDuration(Duration.seconds(0.5));
            }

            @Override
            protected void interpolate(double frac) {
                VBoxMenu.setTranslateX(-VBoxMenu.getWidth() * (1 - frac));
            }
        };
        transition.play();

        // Анимация перемещения paneWithInfo и PaneName
        TranslateTransition infoTransition = new TranslateTransition(Duration.seconds(0.5), myAnchorePane);
        infoTransition.setToX(0); // Возвращаем paneWithInfo в исходное положение
        infoTransition.play();

        TranslateTransition nameTransition = new TranslateTransition(Duration.seconds(0.5), namePage);
        nameTransition.setToX(0); // Возвращаем PaneName в исходное положение
        nameTransition.play();

        TranslateTransition btnTransition = new TranslateTransition(Duration.seconds(0.5), deleteBtnPane);
        btnTransition.setToX(0); // Возвращаем PaneName в исходное положение
        btnTransition.play();

        openMenu.setVisible(false); // Скрываем кнопку openMenu
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

        closeMenu.setOnMouseClicked(event -> {
            slideOutMenu(); // Вызываем метод для анимации закрытия меню
        });

        openMenu.setOnMouseClicked(event -> {
            slideInMenu(); // Вызываем метод для анимации открытия меню
        });

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