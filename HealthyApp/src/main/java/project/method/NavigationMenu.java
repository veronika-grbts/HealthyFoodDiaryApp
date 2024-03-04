package project.method;

import project.controller.HibbernateRunner;

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
