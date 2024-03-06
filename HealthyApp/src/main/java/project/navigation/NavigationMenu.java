package project.navigation;

import project.HibbernateRunner;

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