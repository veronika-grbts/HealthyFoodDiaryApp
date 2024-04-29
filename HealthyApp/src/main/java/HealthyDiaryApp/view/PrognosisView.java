package HealthyDiaryApp.view;

import HealthyDiaryApp.controller.PrognosisController;
import HealthyDiaryApp.navigation.BaseMenuClass;
import HealthyDiaryApp.navigation.MouseEnterHandler;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PrognosisView extends BaseMenuClass {
    private PrognosisController  prognosisController = new PrognosisController();

    @FXML
    private HBox hBoxTime;

    @FXML
    private Label daysLabel;

    @FXML
    private Label hoursLabel;

    @FXML
    private Label minutesLabel;

    @FXML
    private Label secondsLabel;

    @FXML
    private Button firstDataBtn;

    @FXML
    private Button currentDataBtn;

    @FXML
    private Button yourselfChoiceBtn;

    @FXML
    private AnchorPane firstWeightData;

    @FXML
    private TextField ageUser;

    @FXML
    private TextField currentWeightUser;

    @FXML
    private TextField heightUser;

    @FXML
    private TextField levelActivityUser;

    @FXML
    private AnchorPane currentDataUser;

    @FXML
    private TextField ageUserPageCurrentData;

    @FXML
    private TextField nowWeightPageCurrentData;

    @FXML
    private TextField heightUserPageCurrentData;

    @FXML
    private TextField levelActivityUserPageCurrentData;

    @FXML
    private AnchorPane dataChoiceYourself;

    @FXML
    private TextField agePageChoiceYourself;

    @FXML
    private TextField weightUserPageChoieceYourself;

    @FXML
    private TextField heighPageChoiceYourself;

    @FXML
    private ChoiceBox<String> activityLevelPageChoiceYourself;

    @FXML
    private Button countPageChoiceData;

    @FXML
    private Label textNameGraphics;

    @FXML
    private ImageView backGraphic;

    @FXML
    private ImageView nextGrafic;

    @FXML
    private AnchorPane chartPane;

    @FXML
    private AnchorPane pleceForDiscribe;

    @FXML
    private AnchorPane graphicBMI;

    @FXML
    private AnchorPane barChar;

    @FXML
    private AnchorPane userLowWeightPage;

    @FXML
    private ImageView closeWindowLowWeightImg;

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
    private ImageView standClock;

    @FXML
    private ImageView maximizeAppImg;

    @FXML
    void ClosePane(MouseEvent event) {

    }

    @FXML
    void ClosePaneFinishedLossWeightImg(MouseEvent event) {

    }

    @FXML
    void ClosePaneUserChoseIncorectWeight(MouseEvent event) {

    }

    @FXML
    void ClosePaneWithIdealWeight(MouseEvent event) {

    }

    @FXML
    void ClosePaneWithLowWeight(MouseEvent event) {

    }

    private void setMouseHandlers() {
        MouseEnterHandler.addMouseEnterHandler(loseWeightMenuButton, forecastMenuItem, progresisMenuItem, changePageBtn);
    }

    @FXML
    void initialize() {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton, forecastMenuItem, progresisMenuItem);
        setMouseHandlers(); // Установить обработчики событий наведения и убирания мыши
        // Варианты ответов
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), standClock);
        ObservableList<String> activityLevels = FXCollections.observableArrayList(
                "Низкий", "Средний", "Высокий"
        );

        // Установка вариантов ответов в чойсбокс
        activityLevelPageChoiceYourself.setItems(activityLevels);
        prognosisController.initialized(chartPane, graphicBMI, barChar, currentWeightUser, ageUser, heightUser,
                levelActivityUser, textNameGraphics, pleceForDiscribe, nextGrafic, true, activityLevelPageChoiceYourself);

        // Обработчик для кнопки firstDataBtn
        firstDataBtn.setOnAction(event -> {
            rotateTransition.stop();
            standClock.setVisible(false);

            pleceForDiscribe.getChildren().clear();
            chartPane.getChildren().clear(); // Очистить панель с графиком
            graphicBMI.getChildren().clear(); // Очистить панель с графиком BMI
            barChar.getChildren().clear(); // Очистить панель с столбчатой диаграммой

            prognosisController.initialized(chartPane, graphicBMI, barChar, currentWeightUser, ageUser, heightUser,
                    levelActivityUser, textNameGraphics, pleceForDiscribe, nextGrafic, true, activityLevelPageChoiceYourself);
            firstWeightData.setVisible(true); // Отобразить панель с первоначальными данными
            currentDataUser.setVisible(false); // Скрыть панель с текущими данными
            dataChoiceYourself.setVisible(false); // Скрыть панель с выбором данных самостоятельно
        });

        // Обработчик для кнопки yourselfChoiceBtn
        yourselfChoiceBtn.setOnAction(event -> {

            pleceForDiscribe.getChildren().clear();
            chartPane.getChildren().clear(); // Очистить панель с графиком
            graphicBMI.getChildren().clear(); // Очистить панель с графиком BMI
            barChar.getChildren().clear(); // Очистить панель с столбчатой диаграммой

            firstWeightData.setVisible(false); // Скрыть панель с первоначальными данными
            currentDataUser.setVisible(false); // Скрыть панель с текущими данными
            dataChoiceYourself.setVisible(true); // Отобразить панель с выбором данных самостоятельно
            pleceForDiscribe.setVisible(false);
            standClock.setVisible(true);
            // Создание анимации для изображения standClock

            rotateTransition.setByAngle(360); // Поворот на 360 градусов
            rotateTransition.setCycleCount(Timeline.INDEFINITE); // Зацикливание анимации
            rotateTransition.setInterpolator(Interpolator.LINEAR); // Линейная интерполяция для плавного движения
            rotateTransition.play();

        });

        countPageChoiceData.setOnAction(event1 -> {
            rotateTransition.stop();
            standClock.setVisible(false);
            pleceForDiscribe.setVisible(true);
            prognosisController.initialized(chartPane, graphicBMI, barChar, weightUserPageChoieceYourself,
                    agePageChoiceYourself, heighPageChoiceYourself,
                    levelActivityUser, textNameGraphics, pleceForDiscribe, nextGrafic, false, activityLevelPageChoiceYourself);
        });

        // Обработчик для кнопки currentDataBtn
        currentDataBtn.setOnAction(event -> {
            rotateTransition.stop();
            standClock.setVisible(false);

            pleceForDiscribe.getChildren().clear();
            chartPane.getChildren().clear(); // Очистить панель с графиком
            graphicBMI.getChildren().clear(); // Очистить панель с графиком BMI
            barChar.getChildren().clear(); // Очистить панель с столбчатой диаграммой

            prognosisController.initialized(chartPane, graphicBMI, barChar, nowWeightPageCurrentData,
                    ageUserPageCurrentData, heightUserPageCurrentData,
                    levelActivityUserPageCurrentData, textNameGraphics, pleceForDiscribe, nextGrafic, false, activityLevelPageChoiceYourself);
            firstWeightData.setVisible(false); // Скрыть панель с первоначальными данными
            currentDataUser.setVisible(true); // Отобразить панель с текущими данными
            dataChoiceYourself.setVisible(false); // Скрыть панель с выбором данных самостоятельно
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


    }
}
