/*
 * LoseWeightController  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description:Контролер, що відповідає за відображення та оновлення інформації про втрату ваги користувача.
 */
package HealthyDiaryApp.controller;

import HealthyDiaryApp.calculator.CalorieCalculator;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.enums.BmiStage;
import HealthyDiaryApp.calculator.LoseWeightCalculator;
import HealthyDiaryApp.singleton.ApplicationContext;
import HealthyDiaryApp.util.UserComponent;
import HealthyDiaryApp.util.WeightLossGoalsComponent;
import javafx.scene.layout.HBox;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
public class LoseWeightController {
    private WeightLossGoalsComponent weightLossGoalsComponent = new WeightLossGoalsComponent();
    private UserComponent userComponent = new UserComponent();
    private Timeline timeline;
    private static final double LOWER_LIMIT_OF_NORMAL_BMI = 18.5;
    private static final double LOWER_LIMIT_OF_EXCESSIVE_BMI = 25;
    private static final double LOWER_LIMIT_OF_OBESITY_ONE_BMI = 30;
    private CountdownTimer countdownTimer;


    public void initializeUserInfo(TextField bodyMassIndexFieldText,
                                   Label nameBodyMassIndexLable, Label descriptionBodyMassIndexLable,
                                   AnchorPane userLowWeightPage,
                                   AnchorPane userIdealWeightPage,
                                   TextField bestweightFieldText, TextField currentCaloricIntakeTextField,
                                   AnchorPane oopsWindowPane, Button updateCouseInPageBtn,
                                   AnchorPane updateCouseUserPage, PieChart diagramAboutWeightLossStage,
                                   HBox hBoxTime, Label daysLabel, Label hoursLabel,
                                   Label minutesLabel, Label secondsLabel,
                                   AnchorPane userFinishedLossWeighttPage) {

        User user = getUserFromApplicationContext();
        double bmi = CountBmi(user);
        bodyMassIndexFieldText.setText(String.valueOf(bmi));
        nameBodyMassIndexLable.setText(getBMIStatus(bmi));
        descriptionBodyMassIndexLable.setText(getBMIStatusDescription(bmi));

        if (bmi < LOWER_LIMIT_OF_NORMAL_BMI) {
            userLowWeightPage.setVisible(true);
        } else if (bmi == LOWER_LIMIT_OF_NORMAL_BMI) {
            userIdealWeightPage.setVisible(true);
        } else {
            if (user.isCauseUser()) {
                bestweightFieldText.setText(String.valueOf(weightLossGoalsComponent.getTargetWeightByPhoneNumber(user.getPhoneNumber())));
                if (weightLossGoalsComponent.getCaloricIntakeByPhoneNumber(user.getPhoneNumber()) != null) {
                    currentCaloricIntakeTextField.setText(String.valueOf(weightLossGoalsComponent.getCaloricIntakeByPhoneNumber(user.getPhoneNumber())));
                }else {
                    currentCaloricIntakeTextField.setText(String.valueOf(weightLossGoalsComponent.getCalociesDayWithDeficitByPhoneNumber(user.getPhoneNumber())));
                }
                if (weightLossGoalsComponent.isLastWeightLossGoalAchieved(user.getPhoneNumber())) {

                    currentCaloricIntakeTextField.setText(String.valueOf(user.getTotalCaloricUser()));
                    // Получаем дату завершения цели из базы данных
                    Date finishDate = weightLossGoalsComponent.getDataFinishGoalsByPhoneNumber(user.getPhoneNumber());

                    // Преобразовываем дату завершения цели в LocalDateTime
                    LocalDateTime finishDateTime = LocalDateTime.ofInstant(finishDate.toInstant(), ZoneId.systemDefault());

                    // Добавляем один месяц к дате завершения цели
                    finishDateTime = finishDateTime.plusMonths(1);

                    // Создаем экземпляр CountdownTimer с измененной целевой датой
                    countdownTimer = new CountdownTimer(finishDateTime);

                    // Запускаем таймер
                    startTimer(hBoxTime, daysLabel, hoursLabel, minutesLabel,
                            secondsLabel, countdownTimer, userFinishedLossWeighttPage);
                } else {
                    // Если у пользователя нет цели, не делаем ничего
                }

                updatePieChart(diagramAboutWeightLossStage);
            } else {
                oopsWindowPane.setVisible(true);
                updateCouseInPageBtn.setOnAction(event -> updateCouseUserPage.setVisible(true));
            }
        }
    }



    // Метод инициализации обновления цели пользователя
    public void initializeUpdateCourseUser(AnchorPane oopsWindowPane,AnchorPane updateCouseUserPage,
                                           TextField bestweightFieldText, TextField currentCaloricIntakeTextField,
                                           PieChart diagramAboutWeightLossStage,AnchorPane userIdealWeightPage,
                                           AnchorPane updateBestWeightPage) {
            User user = getUserFromApplicationContext();
            oopsWindowPane.setVisible(false);
            updateCouseUserPage.setVisible(false);
            updateBestWeightPage.setVisible(true);
            userComponent.updateUserCause(user.getPhoneNumber(), true);

            double targetWeight = LoseWeightCalculator.calculateBestWeight(user.getHeightUser(), user.isGenderUser());
            double targetCaloricDeficit = LoseWeightCalculator.calculatorDeficitCaloric(user.getWeightUser(), user.getHeightUser(), user.getActivityLevel(), user.getAgeUser(), user.isGenderUser());
            short estimatedCompletionTime = LoseWeightCalculator.estimateTimeToReachGoal(user.getWeightUser(), targetWeight, targetCaloricDeficit);
            double caloriesWithLosingWeight = LoseWeightCalculator.caloriesDayWithDeficit(user.getWeightUser(), user.getHeightUser(), user.getActivityLevel(), user.getAgeUser(), user.isGenderUser());

            if (targetWeight < user.getWeightUser()) {
                weightLossGoalsComponent.saveWeightLossGoal(user, user.getWeightUser(), targetWeight, targetCaloricDeficit, estimatedCompletionTime, caloriesWithLosingWeight);
                bestweightFieldText.setText(String.valueOf(weightLossGoalsComponent.getTargetWeightByPhoneNumber(user.getPhoneNumber())));

                if (String.valueOf(weightLossGoalsComponent.getCaloricIntakeByPhoneNumber(user.getPhoneNumber())).equals("null")) {
                    currentCaloricIntakeTextField.setText(String.valueOf(weightLossGoalsComponent.getCalociesDayWithDeficitByPhoneNumber(user.getPhoneNumber())));
                }else {
                    currentCaloricIntakeTextField.setText(String.valueOf(weightLossGoalsComponent.getCaloricIntakeByPhoneNumber(user.getPhoneNumber())));
                }
                updatePieChart(diagramAboutWeightLossStage);
            }else{
                userIdealWeightPage.setVisible(true);

            }

    }

    public void changeTargetWeight(TextField newBestWeighttextField,
                                   AnchorPane updateBestWeightPage, TextField bestweightFieldText,
                                   PieChart diagramAboutWeightLossStage,
                                   AnchorPane userChoseIncorrectBestWeightPage) {
        User user = getUserFromApplicationContext();
        String newWeightText = newBestWeighttextField.getText();
        if (newWeightText == null || newWeightText.isEmpty()) {
            // Показать окно с ошибкой, если новый вес пустой
            ErrorDialogController.showErrorAlert("Помилка", "Будь-ласка введіть нову оптимальну вагу");
            return; // Прервать выполнение метода, чтобы избежать дальнейших ошибок
        }

        double newWeight = Double.parseDouble(newWeightText);
        double bmi = LoseWeightCalculator.calculateBMI(newWeight, user.getHeightUser());

        // Проверяем, находится ли новый вес в пределах нормального ИМТ
        if (bmi >= LOWER_LIMIT_OF_NORMAL_BMI && bmi < LOWER_LIMIT_OF_EXCESSIVE_BMI) {
            // Сохраняем новый вес
            weightLossGoalsComponent.updateTargetWeightByPhoneNumber(user.getPhoneNumber(), newWeight);
            bestweightFieldText.setText(String.valueOf(newWeight));
            updateBestWeightPage.setVisible(false);
            updatePieChart(diagramAboutWeightLossStage);
        } else {
            // Отображаем страницу с предупреждением об ошибке ввода
            userChoseIncorrectBestWeightPage.setVisible(true);
        }
    }



    // Метод инициализации обновления лучшего веса
    public void initializeUpdateBestWeight(AnchorPane userLowWeightPage,
                                           AnchorPane userIdealWeightPage,
                                           AnchorPane updateBestWeightPage) {

        User user = getUserFromApplicationContext();
        double bmi =  LoseWeightCalculator.calculateBMI(user.getWeightUser(), user.getHeightUser());
        if (bmi < LOWER_LIMIT_OF_NORMAL_BMI) {
            userLowWeightPage.setVisible(true);
        }else if (bmi == LOWER_LIMIT_OF_NORMAL_BMI) {
            userIdealWeightPage.setVisible(true);
        }else {
            updateBestWeightPage.setVisible(true);
        }
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }

    public static String getBMIStatus(double bmi) {
        if (bmi < LOWER_LIMIT_OF_NORMAL_BMI) {
            return BmiStage.UNDERWEIGHT.getLabel();
        } else if (bmi < LOWER_LIMIT_OF_EXCESSIVE_BMI) {
            return BmiStage.NORMAL_WEIGHT.getLabel();
        } else if (bmi < LOWER_LIMIT_OF_OBESITY_ONE_BMI) {
            return BmiStage.OVERWEIGHT.getLabel();
        } else {
            return BmiStage.OBESITY.getLabel();
        }
    }

    // Метод для описания категории ИМТ
    public static String getBMIStatusDescription(double bmi) {
        if (bmi < LOWER_LIMIT_OF_NORMAL_BMI) {
            return BmiStage.UNDERWEIGHT.getDescription();
        } else if (bmi < LOWER_LIMIT_OF_EXCESSIVE_BMI) {
            return BmiStage.NORMAL_WEIGHT.getDescription();
        } else if (bmi < LOWER_LIMIT_OF_OBESITY_ONE_BMI) {
            return BmiStage.OVERWEIGHT.getDescription();
        } else {
            return BmiStage.OBESITY.getDescription();
        }
    }

    private void updatePieChart( PieChart diagramAboutWeightLossStage) {
        User user = getUserFromApplicationContext();
        double targetWeight = weightLossGoalsComponent.getTargetWeightByPhoneNumber(user.getPhoneNumber());
        double progress = 0; // Значение по умолчанию, если нет прогресса
        int oneHundredPercent = 100;
        // Проверяем, есть ли у пользователя прогресс перед его вычислением
        if (targetWeight > 0 ) {
            progress = calculateProgress(targetWeight);
            if(progress > oneHundredPercent){
                progress = oneHundredPercent;
            }
        }
        // Создаем данные для PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Прогрес", progress),
                new PieChart.Data("Залишок", oneHundredPercent - progress)
        );

        // Устанавливаем данные для PieChart
        diagramAboutWeightLossStage.setData(pieChartData);
    }

    // Метод для расчета прогресса пользователя
    private double calculateProgress(double targetWeight) {
        User user = getUserFromApplicationContext();
        Double currentWeight = weightLossGoalsComponent.getCurrentWeightByPhoneNumber(user.getPhoneNumber());

        // Проверяем, если текущий вес равен null, возвращаем 0
        if (currentWeight == null) {
            return 0;
        }

        double totalLoss = currentWeight - targetWeight;
        if (totalLoss == 0){
            return 0;
        }
        double initialLoss = user.getWeightUser() - targetWeight;
        return (initialLoss - totalLoss) / initialLoss * 100;
    }

    private double CountBmi(User user){
        WeightLossGoalsComponent weightLossGoalsComponent = new WeightLossGoalsComponent();
        user = getUserFromApplicationContext();
        double weight = 0;
        double height = user.getHeightUser();
        // Проверяем, есть ли у пользователя запись в таблице WeightLossGoals
        if (weightLossGoalsComponent.hasWeightLossGoal(user.getPhoneNumber())) {
            Double latestProgress = weightLossGoalsComponent.getCurrentWeightByPhoneNumber(user.getPhoneNumber());
            if (latestProgress != null) {
                weight = latestProgress;
            }else{
                weight = weightLossGoalsComponent.getWeightFromGoalsByPhoneNumber(user.getPhoneNumber());
            }
        }else {
            weight = user.getWeightUser();
        }
        // Рассчитываем ИМТ с полученными значениями веса и роста
        return LoseWeightCalculator.calculateBMI(weight, height);
    }

    // Метод для запуска таймера
    private void startTimer(HBox hBoxTime, Label daysLabel, Label hoursLabel,
                            Label minutesLabel, Label secondsLabel, CountdownTimer countdownTimer,
                            AnchorPane userFinishedLossWeighttPage) {
        hBoxTime.setVisible(true);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime timerFinishTime = countdownTimer.getFinishDate();
                // Проверяем, достигнута ли текущая дата или время после установленной даты завершения таймера
                if (currentTime.isAfter(timerFinishTime)) {
                    // Если текущая дата или время после установленной даты завершения таймера, останавливаем таймер
                    stop();
                    // Показываем панель, когда таймер достигает нуля
                    userFinishedLossWeighttPage.setVisible(true);

                    // Устанавливаем значения времени в ноль
                    daysLabel.setText("0");
                    hoursLabel.setText("0");
                    minutesLabel.setText("0");
                    secondsLabel.setText("0");

                    // Пересчитываем калораж и выводим его в консоль
                    recalculateAndPrintCalories();
                    return;
                }else {
                    // Получаем значения времени из countdownTimer
                    String[] timeComponents = countdownTimer.getTimeComponents();
                    daysLabel.setText(timeComponents[0]);
                    hoursLabel.setText(timeComponents[1]);
                    minutesLabel.setText(timeComponents[2]);
                    secondsLabel.setText(timeComponents[3]);
                }
            }
        };
        timer.start();
    }

    // Метод для пересчета калоража и вывода его в консоль
    private void recalculateAndPrintCalories() {
        User user = getUserFromApplicationContext();
        double newCalories = CalorieCalculator.calculateCalories(weightLossGoalsComponent.getCurrentWeightByPhoneNumber(user.getPhoneNumber()),
                user.getHeightUser(), user.getAgeUser(), user.isGenderUser(), user.getActivityLevel());
        double newTotalProteinUser = CalorieCalculator.calculateProtein(newCalories);
        double newTotalFatUser = CalorieCalculator.calculateFat(newCalories);
        double newTotalCarbsUser = CalorieCalculator.calculateCarbs(newCalories);
        userComponent.updateUserDataByPhoneNumber(user.getPhoneNumber(), newCalories,newTotalProteinUser, newTotalFatUser, newTotalCarbsUser);
        System.out.println("Новый калораж с учетом снижения веса до нормы: " + newCalories);
    }
}
