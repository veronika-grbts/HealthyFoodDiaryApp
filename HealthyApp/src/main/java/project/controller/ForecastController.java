package project.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
//import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.entity.User;
import project.entity.WeightLossProgress;
import project.method.LoseWeightCalculator;
import project.method.NavigationMenu;
import project.singleton.ApplicationContext;
import project.util.HibernateMethods;
import java.sql.Date;

public class ForecastController {
    private HibernateMethods hibernateMethods = new HibernateMethods();
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
        User user = getUserFromApplicationContext();
        // Получение данных из базы данных
        List<Double> weightList = hibernateMethods.fetchWeightLossProgress(user.getPhoneNumber());
        List<java.util.Date> dateList = hibernateMethods.fetchWeightLossDates(user.getPhoneNumber());
        System.out.println(weightList);
        System.out.println(dateList);
        // Форматирование дат в виде месяца и дня (например, "MM.dd")
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");
        List<String> formattedDateList = new ArrayList<>();
        for (java.util.Date date : dateList) {
            String formattedDate = dateFormat.format(date);
            formattedDateList.add(formattedDate);
        }
        // Передача форматированных дат и весов в метод создания графика
        createWeightProgressChart(formattedDateList, weightList);
        updateBestWeightUserBtn.setOnAction(event -> {
            addProgressUserPane.setVisible(true);
            closeWindowAddProgressWeightImg.setOnMouseClicked(this::closeWindowAddProgressWeightImg);
        });

        addNewWeightUserBtn.setOnAction(event -> {
            double newWeightUser = Double.parseDouble(newWeightUserTextFiel.getText());

            double deficitCaloric = LoseWeightCalculator.calculatorDeficitCaloric(
                    newWeightUser, user.getHeightUser(),user.getActivityLevel(),
                    user.getAgeUser(), user.isGenderUser());

            double caloricInTake = LoseWeightCalculator.caloriesDayWithDeficit(
                    newWeightUser,user.getHeightUser(),
                    user.getActivityLevel(),user.getAgeUser(), user.isGenderUser());

            // Получаем сегодняшнюю дату
            LocalDate currentDate = LocalDate.now();

            // Преобразование LocalDate в java.sql.Date
            Date sqlDate = Date.valueOf(currentDate);
            // Вызываем метод для сохранения прогресса
            hibernateMethods.saveWeightLossProgress(user.getPhoneNumber(),
                    sqlDate, newWeightUser, caloricInTake, deficitCaloric);
            addProgressUserPane.setVisible(false);
            ApplicationContext.getInstance()
                    .setCurrentUser(hibernateMethods.getUserByPhoneNumber(user.getPhoneNumber()));

            // Получение обновленных данных из базы данных
            List<Double> updatedWeightList = hibernateMethods.fetchWeightLossProgress(user.getPhoneNumber());
            List<java.util.Date> updatedDateList = hibernateMethods.fetchWeightLossDates(user.getPhoneNumber());

            // Форматирование дат в виде месяца и дня (например, "MM.dd")
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("MM.dd");
            List<String> formattedDateListNew = new ArrayList<>();
            for (java.util.Date date : updatedDateList) {
                String formattedDate = dateFormatNew.format(date);
                formattedDateListNew.add(formattedDate);
            }
            weightProgressChart.getData().clear();
            createWeightProgressChart(formattedDateListNew, updatedWeightList);
        });

        mainPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("mainpage"));
        calculatorPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("calorieCalculator"));
        createdMenuPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("createdMenu"));
        changePageBtn.setOnAction(event -> NavigationMenu.navigateToPage("settings"));
        loseWeightMenuButton.setOnAction(event -> NavigationMenu.navigateToPage("loseWeight"));
        forecastMenuItem.setOnAction(event -> NavigationMenu.navigateToPage("forecast"));

    }

    private void createWeightProgressChart(List<String> formattedDateList, List<Double> weightList) {
        // Создание осей графика
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Дата");
        yAxis.setLabel("Вес");

        // Создание графика
        weightProgressChart.setCreateSymbols(true);

        // Создание серии данных
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Изменение веса");

        // Добавление данных из списка в серию
        for (int i = 0; i < formattedDateList.size(); i++) {
            // Уменьшаем каждое значение веса на 50 и добавляем в серию
            series.getData().add(new XYChart.Data<>(formattedDateList.get(i), weightList.get(i)));
        }
        // Добавление серии на график
        weightProgressChart.getData().add(series);
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }


}