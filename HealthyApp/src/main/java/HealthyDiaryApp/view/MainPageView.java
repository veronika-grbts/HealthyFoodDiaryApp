package HealthyDiaryApp.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
/*
 * MainPageView class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас представляє відображення сторінки "профіль користувача" програми і наслідує клас BaseMenuClass.
 * Клас містить різні поля та методи для взаємодії з елементами вікна та ініціалізації.
 */
import java.util.Timer;
import java.util.TimerTask;

import HealthyDiaryApp.navigation.MouseEnterHandler;
import javafx.animation.*;
import javafx.application.Platform;
import HealthyDiaryApp.navigation.NavigationMenu;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import HealthyDiaryApp.controller.MainPageController;
import HealthyDiaryApp.navigation.BaseMenuClass;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MainPageView extends BaseMenuClass {
    private MainPageController mainPageController = new MainPageController();

    @FXML
    private AnchorPane mainAnchorePane;

    @FXML
    private AnchorPane paneWithInfo;

    @FXML
    private TextField height;

    @FXML
    private TextField name_id;

    @FXML
    private TextField numberPhone;

    @FXML
    private TextField age;

    @FXML
    private TextField weight;

    @FXML
    private TextField gender;

    @FXML
    private TextField allergy;

    @FXML
    private Pane PaneName;

    @FXML
    private Label NamePage;

    @FXML
    private VBox VBoxMenu;

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
    private ImageView openMenu;

    @FXML
    private Button existBtn;


    // Метод для анимации появления элементов
    private void fadeInButton(Button button) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), button);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    // Метод для анимации исчезновения элементов
    private void fadeOutButton(Button button) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), button);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished((ActionEvent event) -> {
            button.setVisible(false);
        });
        fadeTransition.play();
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
        TranslateTransition infoTransition = new TranslateTransition(Duration.seconds(0.5), paneWithInfo);
        infoTransition.setToX(-VBoxMenu.getWidth()); // Перемещаем paneWithInfo влево на ширину меню
        infoTransition.play();

        TranslateTransition nameTransition = new TranslateTransition(Duration.seconds(0.5), NamePage);
        nameTransition.setToX(-VBoxMenu.getWidth()); // Перемещаем PaneName влево на ширину меню
        nameTransition.play();


        TranslateTransition btnTransition = new TranslateTransition(Duration.seconds(0.5), existBtn);
        btnTransition.setToX(-VBoxMenu.getWidth()); // Перемещаем PaneName влево на ширину меню
        btnTransition.play();

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
        TranslateTransition infoTransition = new TranslateTransition(Duration.seconds(0.5), paneWithInfo);
        infoTransition.setToX(0); // Возвращаем paneWithInfo в исходное положение
        infoTransition.play();

        TranslateTransition nameTransition = new TranslateTransition(Duration.seconds(0.5), NamePage);
        nameTransition.setToX(0); // Возвращаем PaneName в исходное положение
        nameTransition.play();

        TranslateTransition btnTransition = new TranslateTransition(Duration.seconds(0.5), existBtn);
        btnTransition.setToX(0); // Возвращаем PaneName в исходное положение
        btnTransition.play();

        openMenu.setVisible(false); // Скрываем кнопку openMenu
    }


    @FXML
    void initialize()  {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton, forecastMenuItem, progresisMenuItem);
        AnimationButton.addHoverAnimation(existBtn);

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

        mainPageController.fillUserData(name_id, numberPhone, age, height, weight, gender, allergy);

        existBtn.setOnAction(event -> {
            NavigationMenu.navigateToPage("primary");
        });

        closeMenu.setOnMouseClicked(event -> {
            slideOutMenu(); // Вызываем метод для анимации закрытия меню
        });

        openMenu.setOnMouseClicked(event -> {
            slideInMenu(); // Вызываем метод для анимации открытия меню
        });
    }

}

