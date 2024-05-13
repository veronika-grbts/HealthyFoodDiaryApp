/*
 * LoseWeightView class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description:Цей клас представляє відображення втрати ваги і наслідує клас BaseMenuClass.
 Клас містить різні поля та методи для взаємодії з елементами вікна та обробки подій кнопок.
 */
package HealthyDiaryApp.view;
import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.navigation.MouseEnterHandler;
import HealthyDiaryApp.singleton.ApplicationContext;
import HealthyDiaryApp.util.UserComponent;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import HealthyDiaryApp.controller.LoseWeightController;
import HealthyDiaryApp.navigation.BaseMenuClass;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class LoseWeightView extends BaseMenuClass {
    private LoseWeightController loseWeightController = new LoseWeightController();
    private UserComponent userComponent = new UserComponent();
    private static final  int AXIS_OFFSET_Y = 100;
    private static final int ANIMATION_DURATION = 500;
    private static final String FEMALE_PHOTOS_DIRECTORY = "C://Users//User//IdeaProjects//HealthyApp//src//main//resources//images//female";
    private static final String MALE_PHOTOS_DIRECTORY= "C://Users//User//IdeaProjects//HealthyApp//src//main//resources//images//male";

    @FXML
    private Label namePage;

    @FXML
    private Button updateBestWeightUserBtn;

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
    private ScrollPane myScrollPane;

    @FXML
    private AnchorPane AnchorePaneinScroll;

    @FXML
    private TextField bodyMassIndexFieldText;

    @FXML
    private Label descriptionBodyMassIndexLable;

    @FXML
    private Label nameBodyMassIndexLable;

    @FXML
    private TextField currentCaloricIntakeTextField;

    @FXML
    private ImageView photoAccount;

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
    private ImageView closemenu;

    @FXML
    private AnchorPane topBtnMenu;

    @FXML
    private ImageView maximizeAppImg;

    @FXML
    private ImageView closeAppImg;

    @FXML
    private ImageView MinimizeAppImg;

    @FXML
    private ImageView openMenu;

    @FXML
    private VBox VBoxMane;

    @FXML
    private AnchorPane paneIMT;

    @FXML
    private AnchorPane paneInfo;

    @FXML
    private AnchorPane panePieChart;

    // Метод для установки обработчиков событий наведения и убирания мыши с кнопки loseWeightMenuButton
    private void setMouseHandlers() {
        MouseEnterHandler.addMouseEnterHandler(loseWeightMenuButton, forecastMenuItem, progresisMenuItem, changePageBtn);
    }

    // Метод для анимации закрытия меню
    private void slideOutMenu() {
        // Анимация исчезновения VBoxMenu
        Transition transition = new Transition() {
            {
                setCycleDuration(Duration.seconds(0.5));
            }

            @Override
            protected void interpolate(double frac) {
                VBoxMane.setTranslateX(-VBoxMane.getWidth() * frac);
            }
        };
        transition.play();

        // Анимация перемещения paneWithInfo и PaneName
        TranslateTransition imtTransition = new TranslateTransition(Duration.seconds(0.5), paneIMT);
        imtTransition.setToX(-VBoxMane.getWidth()); // Перемещаем paneWithInfo влево на ширину меню
        imtTransition.play();

        // Анимация перемещения paneWithInfo и PaneName
        TranslateTransition infoTransition = new TranslateTransition(Duration.seconds(0.5), paneInfo);
        infoTransition.setToX(-VBoxMane.getWidth()); // Перемещаем paneWithInfo влево на ширину меню
        infoTransition.play();

        // Анимация перемещения paneWithInfo и PaneName
        TranslateTransition pieChartTransition = new TranslateTransition(Duration.seconds(0.5), panePieChart);
        pieChartTransition.setToX(-VBoxMane.getWidth()); // Перемещаем paneWithInfo влево на ширину меню
        pieChartTransition.play();

        TranslateTransition nameTransition = new TranslateTransition(Duration.seconds(0.5), namePage);
        nameTransition.setToX(-VBoxMane.getWidth()); // Перемещаем PaneName влево на ширину меню
        nameTransition.play();

        TranslateTransition hBoxTimeTransition = new TranslateTransition(Duration.seconds(0.5), hBoxTime);
        hBoxTimeTransition.setToX(-VBoxMane.getWidth()); // Перемещаем PaneName влево на ширину меню
        hBoxTimeTransition.play();

        TranslateTransition btnTransition = new TranslateTransition(Duration.seconds(0.5), updateBestWeightUserBtn);
        btnTransition.setToX(-VBoxMane.getWidth()); // Перемещаем PaneName влево на ширину меню
        btnTransition.play();

        // Показываем openMenu
        openMenu.setVisible(true);

        // Включаем анимацию появления кнопки openMenu
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), openMenu);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    // Метод для анимации открытия меню
    private void slideInMenu() {
        // Анимация появления VBoxMenu
        Transition transition = new Transition() {
            {
                setCycleDuration(Duration.seconds(0.5));
            }

            @Override
            protected void interpolate(double frac) {
                VBoxMane.setTranslateX(-VBoxMane.getWidth() * (1 - frac));
            }
        };
        transition.play();

        // Анимация перемещения paneWithInfo и PaneName
        TranslateTransition imtTransition = new TranslateTransition(Duration.seconds(0.5), paneIMT);
        imtTransition.setToX(0); // Перемещаем paneWithInfo влево на ширину меню
        imtTransition.play();

        // Анимация перемещения paneWithInfo и PaneName
        TranslateTransition infoTransition = new TranslateTransition(Duration.seconds(0.5), paneInfo);
        infoTransition.setToX(0); // Перемещаем paneWithInfo влево на ширину меню
        infoTransition.play();

        // Анимация перемещения paneWithInfo и PaneName
        TranslateTransition pieChartTransition = new TranslateTransition(Duration.seconds(0.5), panePieChart);
        pieChartTransition.setToX(0); // Перемещаем paneWithInfo влево на ширину меню
        pieChartTransition.play();


        TranslateTransition nameTransition = new TranslateTransition(Duration.seconds(0.5), namePage);
        nameTransition.setToX(0); // Перемещаем PaneName влево на ширину меню
        nameTransition.play();


        TranslateTransition hBoxTimeTransition = new TranslateTransition(Duration.seconds(0.5), hBoxTime);
        hBoxTimeTransition.setToX(0); // Перемещаем PaneName влево на ширину меню
        hBoxTimeTransition.play();

        TranslateTransition btnTransition = new TranslateTransition(Duration.seconds(0.5), updateBestWeightUserBtn);
        btnTransition.setToX(0); // Возвращаем PaneName в исходное положение
        btnTransition.play();

        openMenu.setVisible(false); // Скрываем кнопку openMenu
    }

    @FXML
    void initialize() {
        setRandomUserPhoto();
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton, forecastMenuItem, progresisMenuItem);
        setMouseHandlers(); // Установить обработчики событий наведения и убирания мыши
        AnimationButton.addHoverAnimation(updateBestWeightUserBtn);
        AnimationButton.addFadeAnimation(updateCouseuserBtn);
        AnimationButton.addHoverAnimation(updateBestWeightBtn);

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


        loseWeightController.initializeUserInfo( bodyMassIndexFieldText,
                 nameBodyMassIndexLable,  descriptionBodyMassIndexLable,
                 userLowWeightPage,
                 userIdealWeightPage,
                 bestweightFieldText,  currentCaloricIntakeTextField,
                 oopsWindowPane,  updateCouseInPageBtn,
                 updateCouseUserPage,  diagramAboutWeightLossStage,
                 hBoxTime,  daysLabel,  hoursLabel,
                 minutesLabel,  secondsLabel, userFinishedLossWeighttPage);
        closeWindowIdealWeightImg.setOnMouseClicked(this::ClosePaneWithIdealWeight);

        updateCouseInPageBtn.setOnAction(event -> {
            updateCouseUserPage.setVisible(true);
        });

        updateBestWeightBtn.setOnAction(event -> {
            loseWeightController.changeTargetWeight(newBestWeighttextField,
                    updateBestWeightPage,  bestweightFieldText,
                    diagramAboutWeightLossStage, userChoseIncorrectBestWeightPage);
            closePageUserchoseIncorectBestWeightImg.setOnMouseClicked(this::ClosePaneUserChoseIncorectWeight);
        });

        updateCouseuserBtn.setOnAction(event -> {
            loseWeightController.initializeUpdateCourseUser(oopsWindowPane, updateCouseUserPage,
                     bestweightFieldText, currentCaloricIntakeTextField,
                     diagramAboutWeightLossStage, userIdealWeightPage,updateBestWeightPage);
        });
        updateBestWeightUserBtn.setOnAction(event -> {
            loseWeightController.initializeUpdateBestWeight(userLowWeightPage,
                    userIdealWeightPage, updateBestWeightPage);

            closeWindowImg.setOnMouseClicked(this::ClosePane);
            closeWindowLowWeightImg.setOnMouseClicked(this::ClosePaneWithLowWeight);
            closeWindowIdealWeightImg.setOnMouseClicked(this::ClosePaneWithIdealWeight);
        });

        userShinishedLossWeightImg.setOnMouseClicked(this::ClosePaneFinishedLossWeightImg);

        closemenu.setOnMouseClicked(event -> {
            slideOutMenu(); // Вызываем метод для анимации закрытия меню
        });

        openMenu.setOnMouseClicked(event -> {
            slideInMenu(); // Вызываем метод для анимации открытия меню
        });

    }
    private void setRandomUserPhoto() {
        User user = getUserFromApplicationContext();

        // Определение пола пользователя (допустим, у вас есть метод для этого)
        boolean gender = user.isGenderUser();

        // Выбор случайного фото в зависимости от пола пользователя
        String photoPath;
        if (gender == true) {
           photoPath = getRandomPhotoPathFromDirectory(MALE_PHOTOS_DIRECTORY);
        } else {
            photoPath = getRandomPhotoPathFromDirectory(FEMALE_PHOTOS_DIRECTORY);
        }

        // Установка выбранного фото в ImageView
        Image image = new Image(photoPath);
        photoAccount.setImage(image);
    }

    private String getRandomPhotoPathFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            // Выбор случайного файла из директории
            Random random = new Random();
            int randomIndex = random.nextInt(files.length);
            return files[randomIndex].toURI().toString();
        } else {
            return null;
        }
    }
    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }
    @FXML
    void ClosePane(MouseEvent event) {
        updateBestWeightPage.setVisible(false);
    }

    @FXML
    void ClosePaneWithIdealWeight(MouseEvent event) {
        userIdealWeightPage.setVisible(false);
    }

    @FXML
    void ClosePaneWithLowWeight(MouseEvent event) {
        userLowWeightPage.setVisible(false);
    }

    @FXML
    void ClosePaneUserChoseIncorectWeight(MouseEvent event){userChoseIncorrectBestWeightPage.setVisible(false);}

    @FXML
    void ClosePaneFinishedLossWeightImg(MouseEvent event){
        userFinishedLossWeighttPage.setVisible(false);
    }
}