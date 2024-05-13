package HealthyDiaryApp.view;

import HealthyDiaryApp.controller.CreatedMenuController;
import HealthyDiaryApp.entity.UserSelectedMenu;
import HealthyDiaryApp.model.CustomMenuItem;
import HealthyDiaryApp.navigation.MouseEnterHandler;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import HealthyDiaryApp.navigation.BaseMenuClass;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
/*
 * CreatedMenuView class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас представляє відображення створення меню і наслідує клас BaseMenuClass.
 * Клас містить різні поля та методи для взаємодії з елементами вікна та обробки подій кнопок.
 */

public class CreatedMenuView extends BaseMenuClass {
    private  CreatedMenuController createdMenuController = new CreatedMenuController();
    @FXML
    private MenuItem statisticsMenuItem;

    @FXML
    private TableView<CustomMenuItem> tableProduct;

    @FXML
    private TableColumn<UserSelectedMenu, String> typeMealColumn;

    @FXML
    private TableColumn<UserSelectedMenu, String> nameProductColumn;

    @FXML
    private TableColumn<UserSelectedMenu, Double> quantityColumn;

    @FXML
    private ImageView updateImage;

    @FXML
    private AnchorPane paneWithInfo;


    @FXML
    private Button updateBtn;

    @FXML
    private ComboBox<String> productsComboBox;

    @FXML
    private Button createdMenutBtn;

    @FXML
    private CheckBox checkBoxCreatedPdf;

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
    private AnchorPane tableAnchorePane;


    @FXML
    private ImageView openMenu;


    @FXML
    private ImageView closemenu;


    @FXML
    private VBox VboxMenu;


    @FXML
    private Label namePage;

    // Метод для установки обработчиков событий наведения и убирания мыши с кнопки loseWeightMenuButton
    private void setMouseHandlers() {
        MouseEnterHandler.addMouseEnterHandler(loseWeightMenuButton, forecastMenuItem, progresisMenuItem, changePageBtn);
    }

    @FXML
    void initialize() {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton, forecastMenuItem, progresisMenuItem);
        setMouseHandlers(); // Установить обработчики событий наведения и убирания мыши
        AnimationButton.addFadeAnimation(createdMenutBtn);

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
                stage.setFullScreen(false);
                stage.setWidth(1360); // Устанавливаем ширину окна
                stage.setHeight(720); // Устанавливаем высоту окна

            } else {
                stage.setFullScreen(true);
            }
        });


        closemenu.setOnMouseClicked(event -> {
            slideOutMenu(); // Вызываем метод для анимации закрытия меню
        });

        openMenu.setOnMouseClicked(event -> {
            slideInMenu(); // Вызываем метод для анимации открытия меню
        });

        createdMenuController.initialize(tableProduct, productsComboBox, checkBoxCreatedPdf, updateBtn, updateImage,
                mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton,
                forecastMenuItem, statisticsMenuItem, createdMenutBtn, typeMealColumn, nameProductColumn,
                quantityColumn);

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
                VboxMenu.setTranslateX(-VboxMenu.getWidth() * frac);
            }
        };
        transition.play();

        // Анимация перемещения paneWithInfo и PaneName
        TranslateTransition infoTransition = new TranslateTransition(Duration.seconds(0.5), paneWithInfo);
        infoTransition.setToX(-VboxMenu.getWidth()); // Перемещаем paneWithInfo влево на ширину меню
        infoTransition.play();

        TranslateTransition nameTransition = new TranslateTransition(Duration.seconds(0.5), namePage);
        nameTransition.setToX(-VboxMenu.getWidth()); // Перемещаем PaneName влево на ширину меню
        nameTransition.play();


        TranslateTransition btnTransition = new TranslateTransition(Duration.seconds(0.5), updateBtn);
        btnTransition.setToX(-VboxMenu.getWidth()); // Перемещаем PaneName влево на ширину меню
        btnTransition.play();


        TranslateTransition imgBtnTransition = new TranslateTransition(Duration.seconds(0.5), updateImage);
        imgBtnTransition.setToX(-VboxMenu.getWidth()); // Перемещаем PaneName влево на ширину меню
        imgBtnTransition.play();

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
                VboxMenu.setTranslateX(-VboxMenu.getWidth() * (1 - frac));
            }
        };
        transition.play();

        // Анимация перемещения paneWithInfo и PaneName
        TranslateTransition infoTransition = new TranslateTransition(Duration.seconds(0.5), paneWithInfo);
        infoTransition.setToX(0); // Возвращаем paneWithInfo в исходное положение
        infoTransition.play();

        TranslateTransition nameTransition = new TranslateTransition(Duration.seconds(0.5), namePage);
        nameTransition.setToX(0); // Возвращаем PaneName в исходное положение
        nameTransition.play();

        TranslateTransition btnTransition = new TranslateTransition(Duration.seconds(0.5), updateBtn);
        btnTransition.setToX(0); // Возвращаем PaneName в исходное положение
        btnTransition.play();


        TranslateTransition imgBtnTransition = new TranslateTransition(Duration.seconds(0.5), updateImage);
        imgBtnTransition.setToX(0); // Перемещаем PaneName влево на ширину меню
        imgBtnTransition.play();

        openMenu.setVisible(false); // Скрываем кнопку openMenu
    }

}