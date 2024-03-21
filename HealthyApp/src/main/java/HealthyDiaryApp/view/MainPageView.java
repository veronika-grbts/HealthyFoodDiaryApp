package HealthyDiaryApp.view;

import java.net.URL;
import java.util.ResourceBundle;
/*
 * MainPageView class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас представляє відображення сторінки "профіль користувача" програми і наслідує клас BaseMenuClass.
 * Клас містить різні поля та методи для взаємодії з елементами вікна та ініціалізації.
 */

import HealthyDiaryApp.navigation.NavigationMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import HealthyDiaryApp.controller.MainPageController;
import HealthyDiaryApp.navigation.BaseMenuClass;

public class MainPageView extends BaseMenuClass {
    private MainPageController mainPageController = new MainPageController();
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button mainPageBtn;

    @FXML
    private Button calculatorPageBtn;

    @FXML
    private Button createdMenuPageBtn;

    @FXML
    private Button changePageBtn;

    @FXML
    private SplitMenuButton loseWeightMenuButton;

    @FXML
    private MenuItem forecastMenuItem;

    @FXML
    private MenuItem statisticsMenuItem;


    @FXML
    private TextField name_id;

    @FXML
    private TextField numberPhone;

    @FXML
    private TextField age;

    @FXML
    private TextField height;

    @FXML
    private TextField weight;

    @FXML
    private TextField gender;

    @FXML
    private TextField allergy;

    @FXML
    private Button existBtn;

    @FXML
    void initialize() {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn);
        initializeMenuButton(loseWeightMenuButton);
        initializeMenuItem(forecastMenuItem);
        AnimationButton.addHoverAnimation(existBtn);

        mainPageController.fillUserData(name_id, numberPhone, age,
                height, weight, gender,allergy);

        existBtn.setOnAction(event -> {
                NavigationMenu.navigateToPage("primary");
        });
    }
}

