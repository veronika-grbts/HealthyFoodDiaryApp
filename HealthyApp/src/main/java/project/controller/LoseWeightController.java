package project.controller;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import project.entity.User;
import project.method.BmiStage;
import project.method.LoseWeightCalculator;
import project.method.NavigationMenu;
import project.singleton.ApplicationContext;
import project.util.HibernateMethods;

public class LoseWeightController {
    private HibernateMethods hibernateMethods = new HibernateMethods();

    @FXML
    private Button updateBestWeightUserBtn;

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
    private TextField bodyMassIndexFieldText;

    @FXML
    private Label descriptionBodyMassIndexLable;

    @FXML
    private Label nameBodyMassIndexLable;

    @FXML
    private TextField currentCaloricIntakeTextField;

    @FXML
    private TextField bestweightFieldText;

    @FXML
    private PieChart diagramAboutWeightLossStage;

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
    private ImageView closeWindowLowWeightImg;


    @FXML
    private ImageView closeWindowIdealWeightImg;


    @FXML
    private AnchorPane userLowWeightPage;

    private TranslateTransition transition;

    @FXML
    void ClosePane(MouseEvent event) {
        updateBestWeightPage.setVisible(false);
    }

    @FXML
    void ClosePaneWithLowWeight (MouseEvent event) {
        userLowWeightPage.setVisible(false);
    }

    @FXML
    void ClosePaneWithIdealWeight (MouseEvent event) {
        userIdealWeightPage.setVisible(false);
    }

    @FXML
    void initialize() {
        // Создайте анимацию для кнопки changePageBtn
        transition = new TranslateTransition(Duration.millis(500), changePageBtn);
        transition.setByY(100); // Смещение на 100 вниз
        loseWeightMenuButton.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                transition.playFromStart();
            }
        });

        // Добавьте слушателя событий для меню, чтобы запускать анимацию при его закрытии
        loseWeightMenuButton.setOnHidden(event -> {
            transition.stop();
            changePageBtn.setTranslateY(0);
        });

        User user = getUserFromApplicationContext();
        double bmi = LoseWeightCalculator.calculateBMI(
                user.getWeightUser(), user.getHeightUser());

        bodyMassIndexFieldText.setText(String.valueOf(bmi));
        nameBodyMassIndexLable.setText(getBMIStatus(bmi));
        descriptionBodyMassIndexLable.setText(getBMIStatusDescription(bmi));
        if (bmi < 18.5){
            userLowWeightPage.setVisible(true);
            closeWindowLowWeightImg.setOnMouseClicked(this::ClosePaneWithLowWeight);
        }else if (bmi == 18.5){
            userIdealWeightPage.setVisible(true);
            closeWindowIdealWeightImg.setOnMouseClicked(this::ClosePaneWithIdealWeight);
        } else {
            if(user.isCauseUser()){
                bestweightFieldText.setText(
                        String.valueOf(hibernateMethods.getTargetWeightByPhoneNumber(user.getPhoneNumber()))
                );
                if (hibernateMethods.getCaloricIntakeByPhoneNumber(user.getPhoneNumber()) != null){
                    currentCaloricIntakeTextField.setText(
                            String.valueOf(hibernateMethods.getCaloricIntakeByPhoneNumber(user.getPhoneNumber())));
                }else{
                    currentCaloricIntakeTextField.setText(
                            String.valueOf(hibernateMethods.getCalociesDayWithDeficitByPhoneNumber(user.getPhoneNumber())));
                }
                updatePieChart();
            }else {
                oopsWindowPane.setVisible(true);
                updateCouseInPageBtn.setOnAction(event -> {
                    updateCouseUserPage.setVisible(true);
                });
            }
        }

        updateCouseuserBtn.setOnAction(event -> {
            oopsWindowPane.setVisible(false);
            updateCouseUserPage.setVisible(false);
            hibernateMethods.updateUserCause(user.getPhoneNumber(), true);
            double targetWeight = LoseWeightCalculator.calculateBestWeight(
                    user.getHeightUser(), user.isGenderUser()
            );

            double targetCaloricDeficit = LoseWeightCalculator.calculatorDeficitCaloric(
                    user.getWeightUser(), user.getHeightUser(), user.getActivityLevel(),
                    user.getAgeUser(), user.isGenderUser()
            );

            short estimatedCompletionTime = LoseWeightCalculator.estimateTimeToReachGoal(
                    user.getWeightUser(), targetWeight, targetCaloricDeficit
            );

            double caloriesWithLosingWeight = LoseWeightCalculator.caloriesDayWithDeficit(
                    user.getWeightUser(), user.getHeightUser(), user.getActivityLevel(),
                    user.getAgeUser(), user.isGenderUser()
            );
            if (targetWeight < user.getWeightUser()){
                hibernateMethods.saveWeightLossGoal(user, user.getWeightUser(),
                        targetWeight, targetCaloricDeficit,
                        estimatedCompletionTime, caloriesWithLosingWeight);
                bestweightFieldText.setText(
                        String.valueOf(hibernateMethods.getTargetWeightByPhoneNumber(user.getPhoneNumber())));
                if (String.valueOf(hibernateMethods.getCaloricIntakeByPhoneNumber(user.getPhoneNumber())).equals("null")) {
                    currentCaloricIntakeTextField.setText(
                            String.valueOf(hibernateMethods.getCalociesDayWithDeficitByPhoneNumber(user.getPhoneNumber())));
                } else {
                    currentCaloricIntakeTextField.setText(
                            String.valueOf(hibernateMethods.getCaloricIntakeByPhoneNumber(user.getPhoneNumber())));
                }
                updatePieChart();
            }else{
                userIdealWeightPage.setVisible(true);
                closeWindowLowWeightImg.setOnMouseClicked(this::ClosePaneWithLowWeight);
            }
        });

        updateBestWeightUserBtn.setOnAction(event -> {
            if (bmi < 18.5){
                userLowWeightPage.setVisible(true);
                closeWindowLowWeightImg.setOnMouseClicked(this::ClosePaneWithLowWeight);
            }else if (bmi == 18.5){
                userIdealWeightPage.setVisible(true);
                closeWindowIdealWeightImg.setOnMouseClicked(this::ClosePaneWithIdealWeight);
            }else{
                updateBestWeightPage.setVisible(true);
                closeWindowImg.setOnMouseClicked(this::ClosePane);
            }
        });
        updateBestWeightBtn.setOnAction(event -> {
            hibernateMethods.updateTargetWeightByPhoneNumber(user.getPhoneNumber(),
                    Double.parseDouble(newBestWeighttextField.getText()));
            updateBestWeightPage.setVisible(false);
            bestweightFieldText.setText(
                    String.valueOf(hibernateMethods.getTargetWeightByPhoneNumber(user.getPhoneNumber())));
            updatePieChart();
        });

        mainPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("mainpage"));
        calculatorPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("calorieCalculator"));
        createdMenuPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("createdMenu"));
        changePageBtn.setOnAction(event -> NavigationMenu.navigateToPage("settings"));
        loseWeightMenuButton.setOnAction(event -> NavigationMenu.navigateToPage("loseWeight"));

    }
    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }

    // Метод для определения категории ИМТ
    public static String getBMIStatus(double bmi) {
        if (bmi < 18.5) {
            return BmiStage.UNDERWEIGHT.getLabel();
        } else if (bmi < 25) {
            return BmiStage.NORMAL_WEIGHT.getLabel();
        } else if (bmi < 30) {
            return BmiStage.OVERWEIGHT.getLabel();
        } else {
            return BmiStage.OBESITY.getLabel();
        }
    }

    // Метод для описания категории ИМТ
    public static String getBMIStatusDescription(double bmi) {
        if (bmi < 18.5) {
            return BmiStage.UNDERWEIGHT.getDescription();
        } else if (bmi < 25) {
            return BmiStage.NORMAL_WEIGHT.getDescription();
        } else if (bmi < 30) {
            return BmiStage.OVERWEIGHT.getDescription();
        } else {
            return BmiStage.OBESITY.getDescription();
        }
    }

    private void updatePieChart() {
        User user = getUserFromApplicationContext();
        double targetWeight = hibernateMethods.getTargetWeightByPhoneNumber(user.getPhoneNumber());
        double progress = 0; // Значение по умолчанию, если нет прогресса

        // Проверяем, есть ли у пользователя прогресс перед его вычислением
        if (targetWeight > 0) {
            progress = calculateProgress(targetWeight);
        }

        // Создаем данные для PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Прогресс", progress),
                new PieChart.Data("Остаток", 100 - progress)
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