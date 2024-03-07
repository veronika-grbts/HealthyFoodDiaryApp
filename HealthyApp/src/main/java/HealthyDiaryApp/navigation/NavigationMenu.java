package HealthyDiaryApp.navigation;

import HealthyDiaryApp.HibbernateRunner;

import java.io.IOException;

public class NavigationMenu {
    public static void navigateToPage(String pageName) {
        try {
            HibbernateRunner.setRoot(pageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}