package HealthyDiaryApp.view;

import HealthyDiaryApp.controller.ForecastController;
import HealthyDiaryApp.controller.PrognosisController;
import HealthyDiaryApp.navigation.BaseMenuClass;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PrognosisView extends BaseMenuClass {
    private PrognosisController  prognosisController = new PrognosisController();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private Button changePageBtn;

    @FXML
    private TextField currentCaloricIntakeTextField;

    @FXML
    private TextField bestweightFieldText;

    @FXML
    private TextField currentCaloricIntakeTextField1;

    @FXML
    private TextField currentCaloricIntakeTextField11;

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private AnchorPane oopsWindowPane;

    @FXML
    private Button updateCouseInPageBtn;

    @FXML
    private AnchorPane updateBestWeightPage;

    @FXML
    private ImageView closeWindowImg;

    @FXML
    private TextField newBestWeighttextField;

    @FXML
    private Button updateBestWeightBtn;

    @FXML
    private AnchorPane updateCouseUserPage;

    @FXML
    private ImageView closeWindowUpdateCouseImg;

    @FXML
    private Button updateCouseuserBtn;

    @FXML
    private AnchorPane userIdealWeightPage;

    @FXML
    private ImageView closeWindowIdealWeightImg;

    @FXML
    private AnchorPane userLowWeightPage;

    @FXML
    private ImageView closeWindowLowWeightImg;

    @FXML
    private AnchorPane userChoseIncorrectBestWeightPage;

    @FXML
    private ImageView closePageUserchoseIncorectBestWeightImg;

    @FXML
    private AnchorPane userFinishedLossWeighttPage;

    @FXML
    private ImageView userShinishedLossWeightImg;

    @FXML
    private ImageView closeAppImg;

    @FXML
    private ImageView MinimizeAppImg;

    @FXML
    private AnchorPane chartPane;

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

    @FXML
    void initialize() {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn);
        initializeMenuButton(loseWeightMenuButton);
        initializeMenuItem(forecastMenuItem);


        prognosisController.initialized(chartPane);


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

    }
}
