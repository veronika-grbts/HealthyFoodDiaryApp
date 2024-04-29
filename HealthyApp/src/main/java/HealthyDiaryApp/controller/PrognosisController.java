package HealthyDiaryApp.controller;

import HealthyDiaryApp.calculator.CalorieCalculator;
import HealthyDiaryApp.calculator.LoseWeightCalculator;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.enums.ActivityLevel;
import HealthyDiaryApp.enums.GraphType;
import HealthyDiaryApp.singleton.ApplicationContext;
import HealthyDiaryApp.util.WeightLossGoalsComponent;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class PrognosisController {

    private LoseWeightController loseWeightController = new LoseWeightController();
    private WeightLossGoalsComponent weightLossGoalsComponent = new WeightLossGoalsComponent();
    private LoseWeightCalculator loseWeightCalculator = new LoseWeightCalculator();
    private GraphType currentGraph = GraphType.LINE_CHART;
    private LineChart<Number, Number> lineChart;
    private LineChart<Number, Number> bmiChart;
    private BarChart<String, Number> barChart;

    public void initialized(AnchorPane chartPane, AnchorPane graphicBMI, AnchorPane barChar, TextField currentWeightUser,
                            TextField ageUser, TextField heightUser, TextField activityLevelUser,
                            Label textNameGraphics, AnchorPane pleceForDiscribe,
                            ImageView nextGrafic, boolean useFirstData, ChoiceBox<String> activityLevelPageChoiceYourself) {
        User user = getUserFromApplicationContext();
        ActivityLevel activityLevel;
        String activityLevelText;
        if (currentWeightUser.isEditable()) {
            activityLevelText = activityLevelPageChoiceYourself.getValue();
        }else {
             activityLevelText = activityLevelUser.getText().trim();
        }
        switch (activityLevelText.toLowerCase()) {
            case "низкий":
                activityLevel = ActivityLevel.Low;
                break;
            case "средний":
                activityLevel = ActivityLevel.Medium;
                break;
            case "высокий":
                activityLevel = ActivityLevel.High;
                break;
            default:
                activityLevel = ActivityLevel.Medium;
                break;
        }

        chartPane.getChildren().clear();
        graphicBMI.getChildren().clear();
        barChar.getChildren().clear();

        double weight;
        if (useFirstData) {
            weight = user.getWeightUser();
        }else {
            if (currentWeightUser.isEditable()) {
                weight = Double.parseDouble(currentWeightUser.getText());
                double heigh = Double.parseDouble(heightUser.getText());
                int age = Integer.parseInt(ageUser.getText());

            }else {
                Task<Double> fetchWeightTask = weightLossGoalsComponent.createFetchWeightTask(user.getPhoneNumber());
                fetchWeightTask.setOnSucceeded(event -> {
                    double fetchedWeight = fetchWeightTask.getValue();
                    currentWeightUser.setText(String.valueOf(fetchedWeight) + " кг");
                    ageUser.setText(String.valueOf(user.getAgeUser()));
                    heightUser.setText(String.valueOf(user.getHeightUser()) + " см");
                    activityLevelUser.setText(translateActivityLevel(user.getActivityLevel()));

                    createLineChart(chartPane, textNameGraphics, pleceForDiscribe, parseWeightFromTextField(currentWeightUser),
                            parseWeightFromTextField(heightUser), activityLevel, Integer.parseInt(ageUser.getText()));
                });
                fetchWeightTask.setOnFailed(event -> {
                    Throwable exception = fetchWeightTask.getException();
                    exception.printStackTrace();
                });
                new Thread(fetchWeightTask).start();
                return;
            }
        }

        currentWeightUser.setText(String.valueOf(weight) + " кг");
        ageUser.setText(String.valueOf(user.getAgeUser()));
        heightUser.setText(String.valueOf(user.getHeightUser()) + " см");
        activityLevelUser.setText(translateActivityLevel(user.getActivityLevel()));

        createLineChart(chartPane, textNameGraphics, pleceForDiscribe, parseWeightFromTextField(currentWeightUser),
                parseWeightFromTextField(heightUser), activityLevel, Integer.parseInt(ageUser.getText()));

        nextGrafic.setOnMouseClicked(event -> {
            switch (currentGraph) {
                case LINE_CHART:
                    currentGraph = GraphType.BMI_CHART;
                    break;
                case BMI_CHART:
                    currentGraph = GraphType.BAR_CHART;
                    break;
                case BAR_CHART:
                    currentGraph = GraphType.LINE_CHART;
                    break;
            }
            displayCurrentGraph(chartPane, graphicBMI, barChar, currentWeightUser, ageUser, heightUser, activityLevelUser, textNameGraphics, pleceForDiscribe);
        });
    }

    private void createLineChart(AnchorPane chartPane, Label textNameGraphics, AnchorPane pleceForDiscribe,
                                 double initialWeight, double heightUser,
                                 ActivityLevel activityLevel, int ageUser) {
        User user = getUserFromApplicationContext();

        // Создание осей
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Час (тижні)");
        xAxis.setAutoRanging(false);

        // Установка меток на оси X
        xAxis.setTickUnit(1); // Устанавливаем шаг между метками
        xAxis.setAutoRanging(false); // Отключаем автоматическое масштабирование

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(false);

        yAxis.setLabel("Вага (кг)");

        // Прогноз похудения на основе уровня активности
        //  double initialWeight = user.getWeightUser(); // начальный вес в кг
        double caloriesDeficit = loseWeightCalculator.calculatorDeficitCaloric(initialWeight, heightUser,
               activityLevel, ageUser, user.isGenderUser()); // калорийный дефицит в день

        double sedentaryActivityFactor = 1.2;
        double moderateActivityFactor = 1.5;
        double activeActivityFactor = 1.8;

        // Допустимая погрешность для определения достижения оптимального веса
        double tolerance = 0.1; // Предполагается, что вес измеряется с точностью до десятых

        double OptimalWeight = weightLossGoalsComponent.getTargetWeightByPhoneNumber(user.getPhoneNumber());
        // Настройка оси Y
        double minWeight = OptimalWeight - 5; // Минимальное значение оси Y
        double maxWeight = user.getWeightUser() + 10;
        yAxis.setLowerBound(minWeight);
        yAxis.setUpperBound(maxWeight);

        yAxis.setTickUnit(1); // Устанавливаем шаг между метками

        // Создание графика
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(" ");
        textNameGraphics.setText("Прогнозування втрати ваги за різними рівнями активності");

        // Добавление серии данных для каждого уровня активности
        XYChart.Series<Number, Number> sedentarySeries = new XYChart.Series<>();
        sedentarySeries.setName("Малорухливий");
        XYChart.Series<Number, Number> moderateSeries = new XYChart.Series<>();
        moderateSeries.setName("Помірний");
        XYChart.Series<Number, Number> activeSeries = new XYChart.Series<>();
        activeSeries.setName("Активний");


        // Прогноз похудения до достижения оптимального веса
        int MAX_WEEKS = 100;
        int weeks;
        // Прогноз похудения до достижения оптимального веса
        boolean sedentaryReachedOptimal = false;
        boolean moderateReachedOptimal = false;
        boolean activeReachedOptimal = false;

        // Прогноз похудения до достижения оптимального веса
        for (weeks = 0; weeks <= MAX_WEEKS; weeks++) {
            double sedentaryWeight = initialWeight - ((caloriesDeficit * 7 * weeks) / 7700) * sedentaryActivityFactor;
            double moderateWeight = initialWeight - ((caloriesDeficit * 7 * weeks) / 7700) * moderateActivityFactor;
            double activeWeight = initialWeight - ((caloriesDeficit * 7 * weeks) / 7700) * activeActivityFactor;

            // Проверка достижения оптимального веса для каждой линии отдельно
            if (!sedentaryReachedOptimal && sedentaryWeight <= OptimalWeight + tolerance) {
                sedentaryReachedOptimal = true;
                sedentaryWeight = OptimalWeight;
                sedentarySeries.getData().add(new XYChart.Data<>(weeks, sedentaryWeight));
            } else if (!sedentaryReachedOptimal) {
                sedentarySeries.getData().add(new XYChart.Data<>(weeks, sedentaryWeight));
            }

            if (!moderateReachedOptimal && moderateWeight <= OptimalWeight + tolerance) {
                moderateReachedOptimal = true;
                moderateWeight = OptimalWeight;
                moderateSeries.getData().add(new XYChart.Data<>(weeks, moderateWeight));
            } else if (!moderateReachedOptimal) {
                moderateSeries.getData().add(new XYChart.Data<>(weeks, moderateWeight));
            }

            if (!activeReachedOptimal && activeWeight <= OptimalWeight + tolerance) {
                activeReachedOptimal = true;
                activeWeight = OptimalWeight;
                activeSeries.getData().add(new XYChart.Data<>(weeks, activeWeight));
            } else if (!activeReachedOptimal) {
                activeSeries.getData().add(new XYChart.Data<>(weeks, activeWeight));
            }

            // Если все линии достигли оптимального веса, прерываем цикл
            if (sedentaryReachedOptimal && moderateReachedOptimal && activeReachedOptimal) {
                break;
            }
        }



        int maxXValue = weeks + 3;
        xAxis.setUpperBound(maxXValue);

        TextFlow textFlow = new TextFlow();

        Text sedentaryWeeksText = new Text("Малорухливий: " + sedentarySeries.getData().size() + " тижнів\n");
        Text moderateWeeksText = new Text("Помірний: " + moderateSeries.getData().size() + " тижнів\n");
        Text activeWeeksText = new Text("Активний: " + activeSeries.getData().size() + " тижнів\n");

        // Применение отступа
        textFlow.setPadding(new Insets(5, 5, 0, 5)); // Отступ сверху на 5 пикселей
        textFlow.getChildren().addAll(sedentaryWeeksText, moderateWeeksText, activeWeeksText);
        pleceForDiscribe.getChildren().add(textFlow);

        // Добавление серий данных на график
        lineChart.getData().addAll(sedentarySeries, moderateSeries, activeSeries);

        // Устанавливаем размеры графика
        lineChart.setPrefSize(chartPane.getPrefWidth(), chartPane.getPrefHeight());

        // Добавление графика на AnchorPane
        chartPane.getChildren().add(lineChart);
    }


    private String translateActivityLevel(ActivityLevel activityLevel) {
        switch (activityLevel) {
            case Low:
                return "Низький";
            case Medium:
                return "Середній";
            case High:
                return "Високий";
            default:
                return ""; // Обработка других значений, если необходимо
        }
    }

    public void createBMIChart(AnchorPane graphicBMI, double initialWeight, double optimalWeight,
                               Label textNameGraphics) {
        User user = getUserFromApplicationContext();

        // Создание осей
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Тижні");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Індекс маси тіла (ІМТ)");

        double minWeight = 18.5 - 2; // Минимальное значение оси Y
        double maxWeight = LoseWeightCalculator.calculateBMI(initialWeight, user.getHeightUser()) + 5;
        yAxis.setLowerBound(minWeight);
        yAxis.setUpperBound(maxWeight);
        yAxis.setTickUnit(3);
        yAxis.setAutoRanging(false);

        // Создание графика
        bmiChart = new LineChart<>(xAxis, yAxis);
        bmiChart.setTitle(" ");
        textNameGraphics.setText("Прогнозування зміни ІМТ");

        // Создание серии данных для ИМТ
        XYChart.Series<Number, Number> bmiSeries = new XYChart.Series<>();
        bmiSeries.setName("ІМТ");

        double currentWeight = initialWeight;
        double currentBMI = calculateBMI(currentWeight, user.getHeightUser());
        // Создаем переменную для подсчета итераций
        int count = 0;

        // Добавление данных для каждой недели
        for (int week = 0; week <= 100; week++) {
            // Проверяем, нужно ли добавлять данные для текущей недели
            if (count == 0 || count % 5 == 0) {
                // Добавляем данные в серию для каждой пятой недели
                bmiSeries.getData().add(new XYChart.Data<>(week, currentBMI));
            }

            // Увеличиваем счетчик итераций
            count++;

            // Проверяем, достиг ли ИМТ текущего веса ИМТ оптимального веса с погрешностью
            if (Math.abs(currentBMI - calculateBMI(optimalWeight, user.getHeightUser())) <= 0.1) {
                break; // Прекращаем цикл, если условие выполнено
            }

            double caloriesPerKg = 7000;
            // Общий дефицит калорий в неделю
            double deficitCaloriesPerWeek = (LoseWeightCalculator.calculatorDeficitCaloric(currentWeight,
                    user.getHeightUser(), user.getActivityLevel(), user.getAgeUser(), user.isGenderUser())) * 7;
            // Предполагаемая потеря веса в кг в неделю
            double weightLossPerWeek = deficitCaloriesPerWeek / caloriesPerKg;
            // Предположим, что каждую неделю пользователь теряет
            currentWeight -= weightLossPerWeek;
            currentBMI = calculateBMI(currentWeight, user.getHeightUser());
        }

        int maxXValue = count + 3;
        xAxis.setUpperBound(maxXValue);
        // Добавление серии данных на график
        bmiChart.getData().add(bmiSeries);

        // Устанавливаем размеры графика
        bmiChart.setPrefSize(graphicBMI.getPrefWidth(), graphicBMI.getPrefHeight());

        // Добавление графика на AnchorPane
        graphicBMI.getChildren().add(bmiChart);
    }

    public void createBarChart(AnchorPane barChartPane, double initialWeight, double targetWeight,
                               double height, Label textNameGraphics) {
        // Рассчет начального ИМТ и целевого ИМТ
        double initialBMI = calculateBMI(initialWeight, height);
        double targetBMI = calculateBMI(targetWeight, height);

        // Создание осей
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Создание объекта столбчатой диаграммы
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(" ");
        textNameGraphics.setText("Прогноз кількості тижнів на кожен рівень ІМТ");

        xAxis.setLabel("Ступінь ожиріння");
        yAxis.setLabel("Кількість тижнів");

        // Создание серии данных для столбчатой диаграммы
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        //series.setName("Прогноз потери веса");

        // Рассчет количества недель для каждой степени ожирения
        String[] obesityLevels = {"Норма", "Надлишкова вага", "Ожиріння 1 ступеня", "Ожиріння 2 ступеня", "Ожиріння 3 ступеня"};
        double[] BMIValues = {24.9, 29.9, 34.9, 39.9, Double.MAX_VALUE};

        // Начальные значения
        double currentWeight = initialWeight;
        double currentBMI = initialBMI;
        int weeksToReachTarget = 0;

        // Рассчет количества недель для достижения целевого веса
        while (Math.abs(currentBMI - targetBMI) > 0.1) {
            // Уменьшаем вес на 1 кг в неделю
            currentWeight -= 1;
            currentBMI = calculateBMI(currentWeight, height);
            weeksToReachTarget++;

            // Определение категории текущего ИМТ
            int categoryIndex = 0;
            for (int i = 0; i < BMIValues.length; i++) {
                if (currentBMI <= BMIValues[i]) {
                    categoryIndex = i;
                    break;
                }
            }

            // Увеличиваем значение недель для этой категории
            String category = obesityLevels[categoryIndex];
            series.getData().add(new XYChart.Data<>(category, weeksToReachTarget));
        }

        // Добавление серии данных на диаграмму
        barChart.getData().add(series);

        // Установка размеров графика
        barChart.setPrefSize(barChartPane.getPrefWidth(), barChartPane.getPrefHeight());

        // Добавление графика на AnchorPane
        barChartPane.getChildren().setAll(barChart);
    }

    // Метод для расчета ИМТ
    public double calculateBMI(double weight, double height) {
        double dmi = LoseWeightCalculator.calculateBMI(weight, height);
        return  dmi; // Примерный возвращаемый результат
    }

    private void displayCurrentGraph(AnchorPane chartPane, AnchorPane graphicBMI, AnchorPane barChar, TextField currentWeightUser,
                                     TextField ageUser, TextField heightUser, TextField activityLevelUser,
                                     Label textNameGraphics, AnchorPane pleceForDiscribe) {
        User user = getUserFromApplicationContext();
        ActivityLevel activityLevel;
        String activityLevelText = activityLevelUser.getText().trim(); // Получаем текст из TextField и удаляем пробелы в начале и конце
        // Конвертируем текст в перечисление ActivityLevel
        switch (activityLevelText.toLowerCase()) {
            case "Низький":
                activityLevel = ActivityLevel.Low;
                break;
            case "Середній":
                activityLevel = ActivityLevel.Medium;
                break;
            case "Високий":
                activityLevel = ActivityLevel.High;
                break;
            default:
                // Если введенный текст не соответствует ни одному из перечисленных значений, установим значение по умолчанию
                activityLevel = ActivityLevel.Medium;
                break;
        }

        switch (currentGraph) {
            case LINE_CHART:
                // Удаление предыдущего графика
                if (bmiChart != null) {
                    graphicBMI.getChildren().remove(bmiChart);
                }
                if (barChart != null) {
                    barChar.getChildren().remove(barChart);
                }
                // Отображение нового графика
                createLineChart(chartPane, textNameGraphics, pleceForDiscribe, parseWeightFromTextField(currentWeightUser),
                        parseWeightFromTextField(heightUser), activityLevel, Integer.parseInt(ageUser.getText()));
                break;
            case BMI_CHART:
                // Удаление предыдущего графика
                if (lineChart != null ) {
                    chartPane.getChildren().remove(lineChart);
                }
                if (barChart != null) {
                    barChar.getChildren().remove(barChart);
                }
                // Отображение нового графика
                double optionWeigh = weightLossGoalsComponent.getTargetWeightByPhoneNumber(user.getPhoneNumber());
                createBMIChart(graphicBMI, parseWeightFromTextField(currentWeightUser), optionWeigh, textNameGraphics);
                break;
            case BAR_CHART:
                // Удаление предыдущего графика
                if (lineChart != null) {
                    chartPane.getChildren().remove(lineChart);
                }
                if (bmiChart != null) {
                    graphicBMI.getChildren().remove(bmiChart);
                }
                // Отображение нового графика
                double targetWeigh = weightLossGoalsComponent.getTargetWeightByPhoneNumber(user.getPhoneNumber());

                createBarChart(barChar, parseWeightFromTextField(currentWeightUser), targetWeigh, parseWeightFromTextField(heightUser), textNameGraphics);
                break;
        }
    }

    private double parseWeightFromTextField(TextField textField) {
        String text = textField.getText().trim();
        return Double.parseDouble(text.replaceAll("[^\\d.]", ""));
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }
}
