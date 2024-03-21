package HealthyDiaryApp.navigation;

import HealthyDiaryApp.HibbernateRunner;
/*
 * NavigationMenu class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас забезпечує навігацію між сторінками програми. Він викликає метод
 * setRoot() класу HibbernateRunner для встановлення відображуваної сторінки за її назвою.
 */

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