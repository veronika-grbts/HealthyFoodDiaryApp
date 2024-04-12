package HealthyDiaryApp.navigation;

import HealthyDiaryApp.view.DraggableWindow;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import HealthyDiaryApp.view.AnimationButton;
import javafx.stage.Stage;

/*
 * BaseMenuClass abstract class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас є базовим класом для всіх меню в програмі. Він містить загальні методи
 * для ініціалізації кнопок та обробки подій кнопок у меню.
 */

public abstract class BaseMenuClass {

    protected void initializeButtons(Button... buttons) {
        for (Button button : buttons) {
            AnimationButton.addFadeAnimation(button);
            button.setOnAction(event -> handleButtonAction(button));
        }
    }

    protected void initializeMenuButton(SplitMenuButton menuButton) {
        AnimationButton.addFadeAnimation(menuButton);
        menuButton.setOnAction(event -> handleMenuButtonAction(menuButton));
    }

    protected void initializeMenuItem(MenuItem menuItem) {
        menuItem.setOnAction(event -> handleItemMenuAction(menuItem));
    }

    private void handleButtonAction(Button button) {
        switch (button.getId()) {
            case "mainPageBtn":
                NavigationMenu.navigateToPage("mainpage");
                break;
            case "calculatorPageBtn":
                NavigationMenu.navigateToPage("calorieCalculator");
                break;
            case "createdMenuPageBtn":
                NavigationMenu.navigateToPage("createdMenu");
                break;
            case "changePageBtn":
                NavigationMenu.navigateToPage("settings");
                break;
            case "backBtn":
                NavigationMenu.navigateToPage("primary");
                break;
        }
    }

    private void handleMenuButtonAction(SplitMenuButton menuButton) {
        switch (menuButton.getId()) {
            case "loseWeightMenuButton":
                NavigationMenu.navigateToPage("loseWeight");
                break;
        }
    }

    private void handleItemMenuAction(MenuItem menuItem) {
        switch (menuItem.getId()) {
            case "forecastMenuItem":
                NavigationMenu.navigateToPage("forecast");
                break;
            case "progresisMenuItem":
                NavigationMenu.navigateToPage("prognosis");
                break;

        }
    }

}