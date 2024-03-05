package project.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import project.entity.User;
import project.method.BmiStage;
import project.method.LoseWeightCalculator;
import project.singleton.ApplicationContext;
import project.util.HibernateMethods;

public class LoseWeightController {
    private  HibernateMethods hibernateMethods = new HibernateMethods();
    private static final double LOWER_LIMIT_OF_NORMAL_BMI = 18.5;
    private static final double LOWER_LIMIT_OF_EXCESSIVE_BMI = 25;
    private static final double LOWER_LIMIT_OF_OBESITY_ONE_BMI = 30;
    // Метод инициализации информации о пользователе
    public void initializeUserInfo(TextField bodyMassIndexFieldText,
                                   Label nameBodyMassIndexLable, Label descriptionBodyMassIndexLable,
                                   AnchorPane userLowWeightPage,
                                   AnchorPane userIdealWeightPage,
                                   TextField bestweightFieldText, TextField currentCaloricIntakeTextField,
                                   AnchorPane oopsWindowPane, Button updateCouseInPageBtn,
                                   AnchorPane updateCouseUserPage, PieChart diagramAboutWeightLossStage) {

        User user = getUserFromApplicationContext();
        double bmi = LoseWeightCalculator.calculateBMI(user.getWeightUser(), user.getHeightUser());
        bodyMassIndexFieldText.setText(String.valueOf(bmi));
        nameBodyMassIndexLable.setText(getBMIStatus(bmi));
        descriptionBodyMassIndexLable.setText(getBMIStatusDescription(bmi));

        if (bmi < LOWER_LIMIT_OF_NORMAL_BMI) {
            userLowWeightPage.setVisible(true);
        } else if (bmi == LOWER_LIMIT_OF_NORMAL_BMI) {
            userIdealWeightPage.setVisible(true);
        }else {
            if (user.isCauseUser()) {
                bestweightFieldText.setText(String.valueOf(hibernateMethods.getTargetWeightByPhoneNumber(user.getPhoneNumber())));
                if (hibernateMethods.getCaloricIntakeByPhoneNumber(user.getPhoneNumber()) != null) {
                    currentCaloricIntakeTextField.setText(String.valueOf(hibernateMethods.getCaloricIntakeByPhoneNumber(user.getPhoneNumber())));
                } else {
                    currentCaloricIntakeTextField.setText(String.valueOf(hibernateMethods.getCalociesDayWithDeficitByPhoneNumber(user.getPhoneNumber())));
                }
                updatePieChart(diagramAboutWeightLossStage);
            }else {
                oopsWindowPane.setVisible(true);
                updateCouseInPageBtn.setOnAction(event -> updateCouseUserPage.setVisible(true));
            }
        }
    }

    // Метод инициализации обновления курса пользователя
    public void initializeUpdateCourseUser(AnchorPane oopsWindowPane,AnchorPane updateCouseUserPage,
                                           TextField bestweightFieldText, TextField currentCaloricIntakeTextField,
                                           PieChart diagramAboutWeightLossStage,AnchorPane userIdealWeightPage,
                                           AnchorPane updateBestWeightPage) {
            User user = getUserFromApplicationContext();
            oopsWindowPane.setVisible(false);
            updateCouseUserPage.setVisible(false);
            updateBestWeightPage.setVisible(true);
            hibernateMethods.updateUserCause(user.getPhoneNumber(), true);

            double targetWeight = LoseWeightCalculator.calculateBestWeight(user.getHeightUser(), user.isGenderUser());
            double targetCaloricDeficit = LoseWeightCalculator.calculatorDeficitCaloric(user.getWeightUser(), user.getHeightUser(), user.getActivityLevel(), user.getAgeUser(), user.isGenderUser());
            short estimatedCompletionTime = LoseWeightCalculator.estimateTimeToReachGoal(user.getWeightUser(), targetWeight, targetCaloricDeficit);
            double caloriesWithLosingWeight = LoseWeightCalculator.caloriesDayWithDeficit(user.getWeightUser(), user.getHeightUser(), user.getActivityLevel(), user.getAgeUser(), user.isGenderUser());

            if (targetWeight < user.getWeightUser()) {
                hibernateMethods.saveWeightLossGoal(user, user.getWeightUser(), targetWeight, targetCaloricDeficit, estimatedCompletionTime, caloriesWithLosingWeight);
                bestweightFieldText.setText(String.valueOf(hibernateMethods.getTargetWeightByPhoneNumber(user.getPhoneNumber())));

                if (String.valueOf(hibernateMethods.getCaloricIntakeByPhoneNumber(user.getPhoneNumber())).equals("null")) {
                    currentCaloricIntakeTextField.setText(String.valueOf(hibernateMethods.getCalociesDayWithDeficitByPhoneNumber(user.getPhoneNumber())));
                }else {
                    currentCaloricIntakeTextField.setText(String.valueOf(hibernateMethods.getCaloricIntakeByPhoneNumber(user.getPhoneNumber())));
                }
                updatePieChart(diagramAboutWeightLossStage);
            } else {
                userIdealWeightPage.setVisible(true);

            }

    }

    public void changeTargetWeight(TextField newBestWeighttextField, AnchorPane updateBestWeightPage, TextField bestweightFieldText, PieChart diagramAboutWeightLossStage){
        User user = getUserFromApplicationContext();
        hibernateMethods.updateTargetWeightByPhoneNumber(user.getPhoneNumber(), Double.parseDouble(newBestWeighttextField.getText()));
        updateBestWeightPage.setVisible(false);
        bestweightFieldText.setText(String.valueOf(hibernateMethods.getTargetWeightByPhoneNumber(user.getPhoneNumber())));
        updatePieChart(diagramAboutWeightLossStage);
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
        double targetWeight = hibernateMethods.getTargetWeightByPhoneNumber(user.getPhoneNumber());
        double progress = 0; // Значение по умолчанию, если нет прогресса
        int oneHundredPercent = 100;
        // Проверяем, есть ли у пользователя прогресс перед его вычислением
        if (targetWeight > 0) {
            progress = calculateProgress(targetWeight);
        }

        // Создаем данные для PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Прогресс", progress),
                new PieChart.Data("Остаток", oneHundredPercent - progress)
        );

        // Устанавливаем данные для PieChart
        diagramAboutWeightLossStage.setData(pieChartData);
    }

    // Метод для расчета прогресса пользователя
    private double calculateProgress(double targetWeight) {
        User user = getUserFromApplicationContext();
        Double currentWeight = hibernateMethods.getCurrentWeightByPhoneNumber(user.getPhoneNumber());

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


}
