package HealthyDiaryApp.controller;

import HealthyDiaryApp.calculator.LoseWeightCalculator;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.singleton.ApplicationContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

public class PrognosisController {
    private LoseWeightCalculator loseWeightController = new LoseWeightCalculator();
    public void initialized(AnchorPane chartPane){
        createLineChart(chartPane);
    }
    private void createLineChart(AnchorPane chartPane) {
        User user = getUserFromApplicationContext();

        // Создание осей
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Час (тижні)");

        // Установка меток на оси X
        xAxis.setTickUnit(1); // Устанавливаем шаг между метками
        xAxis.setAutoRanging(false); // Отключаем автоматическое масштабирование

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Вага (кг)");

        // Создание графика
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Прогнозування втрати ваги за різними рівнями активності");

        // Добавление серии данных для каждого уровня активности
        XYChart.Series<Number, Number> sedentarySeries = new XYChart.Series<>();
        sedentarySeries.setName("Малорухливий");
        XYChart.Series<Number, Number> moderateSeries = new XYChart.Series<>();
        moderateSeries.setName("Помірний");
        XYChart.Series<Number, Number> activeSeries = new XYChart.Series<>();
        activeSeries.setName("Активний");

        // Прогноз похудения на основе уровня активности
        double initialWeight = user.getWeightUser(); // начальный вес в кг
        double caloriesDeficit = loseWeightController.calculatorDeficitCaloric(user.getWeightUser(), user.getHeightUser(),
                user.getActivityLevel(), user.getAgeUser(), user.isGenderUser()); // калорийный дефицит в день

        double sedentaryActivityFactor = 1.2;
        double moderateActivityFactor = 1.5;
        double activeActivityFactor = 1.8;

        // Допустимая погрешность для определения достижения оптимального веса
        double tolerance = 0.1; // Предполагается, что вес измеряется с точностью до десятых

        double OptimalWeight = 45;
        // Прогноз похудения до достижения оптимального веса
        int MAX_WEEKS = 100;
        // Прогноз похудения до достижения оптимального веса
        for (int weeks = 0; weeks <= MAX_WEEKS; weeks++) {
            double sedentaryWeight = initialWeight - ((caloriesDeficit * 7 * weeks) / 7700) * sedentaryActivityFactor;
            double moderateWeight = initialWeight - ((caloriesDeficit * 7 * weeks) / 7700) * moderateActivityFactor;
            double activeWeight = initialWeight - ((caloriesDeficit * 7 * weeks) / 7700) * activeActivityFactor;

            // Проверка достижения оптимального веса для каждой линии отдельно
            if (sedentaryWeight <= OptimalWeight + tolerance) {
                sedentaryWeight = OptimalWeight;
                sedentarySeries.getData().add(new XYChart.Data<>(weeks, sedentaryWeight));
                break;
            }
            if (moderateWeight <= OptimalWeight + tolerance) {
                moderateWeight = OptimalWeight;
                moderateSeries.getData().add(new XYChart.Data<>(weeks, moderateWeight));
                break;
            }
            if (activeWeight <= OptimalWeight + tolerance) {
                activeWeight = OptimalWeight;
                activeSeries.getData().add(new XYChart.Data<>(weeks, activeWeight));
                break;
            }

            // Добавление точек на график
            sedentarySeries.getData().add(new XYChart.Data<>(weeks, sedentaryWeight));
            moderateSeries.getData().add(new XYChart.Data<>(weeks, moderateWeight));
            activeSeries.getData().add(new XYChart.Data<>(weeks, activeWeight));
        }

        // Добавление серий данных на график
        chart.getData().addAll(sedentarySeries, moderateSeries, activeSeries);

        // Устанавливаем размеры графика
        chart.setPrefSize(chartPane.getPrefWidth(), chartPane.getPrefHeight());

        // Добавление графика на AnchorPane
        chartPane.getChildren().add(chart);
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }
}
