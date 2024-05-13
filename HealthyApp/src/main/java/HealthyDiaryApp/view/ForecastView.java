package HealthyDiaryApp.view;

import java.net.URL;
import java.util.*;
//import java.util.Date;
import HealthyDiaryApp.navigation.MouseEnterHandler;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import HealthyDiaryApp.controller.ForecastController;
import HealthyDiaryApp.navigation.BaseMenuClass;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * ForecastView class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description:Цей клас представляє відображення  статистики і наслідує клас BaseMenuClass.
 * Клас містить різні поля та методи для взаємодії з елементами вікна та обробки подій кнопок.
 */

public class ForecastView extends BaseMenuClass {
    private ForecastController forecastController = new ForecastController();

    @FXML
    private LineChart<String, Number> weightProgressChart;

    @FXML
    private Label namePage;

    @FXML
    private AnchorPane pageInfo;

    @FXML
    private AnchorPane progressPane;

    @FXML
    private TextField sizeDayDuringLoseWeight;

    @FXML
    private TextField howUserAlredyLossWeight;

    @FXML
    private TextField kgWhatContinue;

    @FXML
    private Label supportText;

    @FXML
    private AnchorPane userIdealWeightPage;

    @FXML
    private ImageView closeWindowIdealWeightImg;

    @FXML
    private AnchorPane userLowWeightPage;

    @FXML
    private ImageView closeWindowLowWeightImg;

    @FXML
    private Button updateBestWeightUserBtn;

    @FXML
    private AnchorPane addProgressUserPane;

    @FXML
    private ImageView closeWindowAddProgressWeightImg;

    @FXML
    private TextField newWeightUserTextFiel;

    @FXML
    private Button addNewWeightUserBtn;

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
    private ImageView closemenu;

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
    void ClosePaneWithLowWeight (MouseEvent event) {
        userLowWeightPage.setVisible(false);
    }

    @FXML
    void ClosePaneWithIdealWeight (MouseEvent event) {
        userIdealWeightPage.setVisible(false);
    }

    @FXML
    void closeWindowAddProgressWeightImg (MouseEvent event) {
        addProgressUserPane.setVisible(false);
    }
    // Метод для установки обработчиков событий наведения и убирания мыши с кнопки loseWeightMenuButton
    private void setMouseHandlers() {
        MouseEnterHandler.addMouseEnterHandler(loseWeightMenuButton, forecastMenuItem, progresisMenuItem, changePageBtn);
    }

    @FXML
    void initialize() {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton, forecastMenuItem, progresisMenuItem);
        setMouseHandlers(); // Установить обработчики событий наведения и убирания мыши
        AnimationButton.addHoverAnimation(updateBestWeightUserBtn);
        AnimationButton.addFadeAnimation(addNewWeightUserBtn);

        forecastController.initialize(weightProgressChart, progressPane,
                sizeDayDuringLoseWeight, howUserAlredyLossWeight, kgWhatContinue,supportText);

        addNewWeightUserBtn.setOnAction(event -> {
            forecastController.addNewWeightUser(newWeightUserTextFiel,
                     addProgressUserPane, weightProgressChart,progressPane);
        });
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


        // Определение действий кнопки обновления лучшего веса
        updateBestWeightUserBtn.setOnAction(event -> {
            addProgressUserPane.setVisible(true);
            closeWindowAddProgressWeightImg.setOnMouseClicked(this::closeWindowAddProgressWeightImg);
        });

        closemenu.setOnMouseClicked(event -> {
            slideOutMenu(); // Вызываем метод для анимации закрытия меню
        });

        openMenu.setOnMouseClicked(event -> {
            slideInMenu(); // Вызываем метод для анимации открытия меню
        });
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
        TranslateTransition infoTransition = new TranslateTransition(Duration.seconds(0.5), pageInfo);
        infoTransition.setToX(-VBoxMenu.getWidth()); // Перемещаем paneWithInfo влево на ширину меню
        infoTransition.play();

        TranslateTransition nameTransition = new TranslateTransition(Duration.seconds(0.5), namePage);
        nameTransition.setToX(-VBoxMenu.getWidth()); // Перемещаем PaneName влево на ширину меню
        nameTransition.play();


        TranslateTransition btnTransition = new TranslateTransition(Duration.seconds(0.5), updateBestWeightUserBtn);
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
        TranslateTransition infoTransition = new TranslateTransition(Duration.seconds(0.5), pageInfo);
        infoTransition.setToX(0); // Возвращаем paneWithInfo в исходное положение
        infoTransition.play();

        TranslateTransition nameTransition = new TranslateTransition(Duration.seconds(0.5), namePage);
        nameTransition.setToX(0); // Возвращаем PaneName в исходное положение
        nameTransition.play();

        TranslateTransition btnTransition = new TranslateTransition(Duration.seconds(0.5), updateBestWeightUserBtn);
        btnTransition.setToX(0); // Возвращаем PaneName в исходное положение
        btnTransition.play();


        openMenu.setVisible(false); // Скрываем кнопку openMenu
    }
}