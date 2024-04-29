package HealthyDiaryApp.view;

import HealthyDiaryApp.controller.CreatedMenuController;
import HealthyDiaryApp.entity.UserSelectedMenu;
import HealthyDiaryApp.model.CustomMenuItem;
import HealthyDiaryApp.navigation.MouseEnterHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import HealthyDiaryApp.navigation.BaseMenuClass;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/*
 * CreatedMenuView class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас представляє відображення створення меню і наслідує клас BaseMenuClass.
 * Клас містить різні поля та методи для взаємодії з елементами вікна та обробки подій кнопок.
 */

public class CreatedMenuView extends BaseMenuClass {
    private  CreatedMenuController createdMenuController = new CreatedMenuController();
    @FXML
    private MenuItem statisticsMenuItem;

    @FXML
    private TableView<CustomMenuItem> tableProduct;

    @FXML
    private TableColumn<UserSelectedMenu, String> typeMealColumn;

    @FXML
    private TableColumn<UserSelectedMenu, String> nameProductColumn;

    @FXML
    private TableColumn<UserSelectedMenu, Double> quantityColumn;

    @FXML
    private ImageView updateImage;

    @FXML
    private Button updateBtn;

    @FXML
    private ComboBox<String> productsComboBox;

    @FXML
    private Button createdMenutBtn;

    @FXML
    private CheckBox checkBoxCreatedPdf;

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
    private ImageView maximizeAppImg;

    @FXML
    private AnchorPane tableAnchorePane;

    // Метод для установки обработчиков событий наведения и убирания мыши с кнопки loseWeightMenuButton
    private void setMouseHandlers() {
        MouseEnterHandler.addMouseEnterHandler(loseWeightMenuButton, forecastMenuItem, progresisMenuItem, changePageBtn);
    }

    @FXML
    void initialize() {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton, forecastMenuItem, progresisMenuItem);
        setMouseHandlers(); // Установить обработчики событий наведения и убирания мыши
        AnimationButton.addFadeAnimation(createdMenutBtn);

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


        createdMenuController.initialize(tableProduct, productsComboBox, checkBoxCreatedPdf, updateBtn, updateImage,
                mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton,
                forecastMenuItem, statisticsMenuItem, createdMenutBtn, typeMealColumn, nameProductColumn,
                quantityColumn);

    }
}