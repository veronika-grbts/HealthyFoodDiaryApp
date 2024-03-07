package HealthyDiaryApp.controller;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.calculator.LoseWeightCalculator;
import HealthyDiaryApp.singleton.ApplicationContext;
import HealthyDiaryApp.util.UserComponent;
import HealthyDiaryApp.util.WeightLossGoalsComponent;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ForecastController {
    private UserComponent userComponent = new UserComponent();
    private WeightLossGoalsComponent weightLossGoalsComponent = new WeightLossGoalsComponent();


    public void initialize(LineChart<String, Number> weightProgressChart) {
        User user = getUserFromApplicationContext();
        // Получение данных из базы данных
        List<Double> weightList = weightLossGoalsComponent.fetchWeightLossProgress(user.getPhoneNumber());
        List<java.util.Date> dateList = weightLossGoalsComponent.fetchWeightLossDates(user.getPhoneNumber());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");
        List<String> formattedDateList = new ArrayList<>();
        for (java.util.Date date : dateList) {
            String formattedDate = dateFormat.format(date);
            formattedDateList.add(formattedDate);
        }
        createWeightProgressChart(formattedDateList, weightList, weightProgressChart);
    }

    private void createWeightProgressChart(List<String> formattedDateList,
                                           List<Double> weightList,
                                           LineChart<String, Number> weightProgressChart) {
        // Создание осей графика
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Дата");
        yAxis.setLabel("Вес");

        // Создание графика
        weightProgressChart.setCreateSymbols(true);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Изменение веса");
        for (int i = 0; i < formattedDateList.size(); i++) {
            // Уменьшаем каждое значение веса на 50 и добавляем в серию
            series.getData().add(new XYChart.Data<>(formattedDateList.get(i), weightList.get(i)));
        }
        weightProgressChart.getData().add(series);
    }

    public void addNewWeightUser(TextField newWeightUserTextFiel,
                                 AnchorPane addProgressUserPane,
                                 LineChart<String, Number> weightProgressChart){
        User user = getUserFromApplicationContext();
        double newWeightUser = Double.parseDouble(newWeightUserTextFiel.getText());

        // Расчет дефицита калорий и калорийного потребления для нового веса
        double deficitCaloric = LoseWeightCalculator.calculatorDeficitCaloric(
                newWeightUser, user.getHeightUser(),user.getActivityLevel(),
                user.getAgeUser(), user.isGenderUser());

        double caloricInTake = LoseWeightCalculator.caloriesDayWithDeficit(
                newWeightUser,user.getHeightUser(),
                user.getActivityLevel(),user.getAgeUser(), user.isGenderUser());


        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);

        weightLossGoalsComponent.saveWeightLossProgress(user.getPhoneNumber(),
                sqlDate, newWeightUser, caloricInTake, deficitCaloric);
        addProgressUserPane.setVisible(false);


        ApplicationContext.getInstance().setCurrentUser(userComponent.getUserByPhoneNumber(user.getPhoneNumber()));
        List<Double> updatedWeightList = weightLossGoalsComponent.fetchWeightLossProgress(user.getPhoneNumber());
        List<java.util.Date> updatedDateList = weightLossGoalsComponent.fetchWeightLossDates(user.getPhoneNumber());


        SimpleDateFormat dateFormatNew = new SimpleDateFormat("MM.dd");
        List<String> formattedDateListNew = new ArrayList<>();
        for (java.util.Date date : updatedDateList) {
            String formattedDate = dateFormatNew.format(date);
            formattedDateListNew.add(formattedDate);
        }


        weightProgressChart.getData().clear();
        createWeightProgressChart(formattedDateListNew, updatedWeightList, weightProgressChart);
    }


    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }
}
