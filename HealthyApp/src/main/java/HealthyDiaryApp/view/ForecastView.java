package HealthyDiaryApp.view;

import java.net.URL;
import java.util.*;
//import java.util.Date;
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
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox VBoxMenu;

    @FXML
    private Button mainPageBtn;

    @FXML
    private Button calculatorPageBtn;

    @FXML
    private Button createdMenuPageBtn;

    @FXML
    private SplitMenuButton loseWeightMenuButton;

    @FXML
    private MenuItem forecastMenuItem;

    @FXML
    private MenuItem statisticsMenuItem;

    @FXML
    private Button changePageBtn;

    @FXML
    private AnchorPane oopsWindowPane;

    @FXML
    private Button updateCouseInPageBtn;

    @FXML
    private LineChart<String, Number> weightProgressChart;

    @FXML
    private AnchorPane userIdealWeightPage;

    @FXML
    private ImageView closeWindowIdealWeightImg;

    @FXML
    private ImageView closeWindowAddProgressWeightImg;
    @FXML
    private AnchorPane userLowWeightPage;

    @FXML
    private ImageView closeWindowLowWeightImg;

    @FXML
    private Button updateBestWeightUserBtn;

    @FXML
    private AnchorPane addProgressUserPane;

    @FXML
    private TextField newWeightUserTextFiel;

    @FXML
    private Button addNewWeightUserBtn;

    @FXML
    private ImageView closeAppImg;

    @FXML
    private ImageView MinimizeAppImg;


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

    @FXML
    void initialize() {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn);
        initializeMenuButton(loseWeightMenuButton);
        initializeMenuItem(forecastMenuItem);

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

        // Определение действий кнопки обновления лучшего веса
        updateBestWeightUserBtn.setOnAction(event -> {
            addProgressUserPane.setVisible(true);
            closeWindowAddProgressWeightImg.setOnMouseClicked(this::closeWindowAddProgressWeightImg);
        });

    }
}