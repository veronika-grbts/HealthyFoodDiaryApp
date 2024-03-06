package project.navigation;

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import project.navigation.NavigationMenu;
import project.view.AnimationButton;

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

        }
    }
}