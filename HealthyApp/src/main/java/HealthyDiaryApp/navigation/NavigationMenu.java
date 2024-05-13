package HealthyDiaryApp.navigation;

import HealthyDiaryApp.HibbernateRunner;
import HealthyDiaryApp.view.WindowSize;
import javafx.stage.Stage;
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
    private static Stage stage; // Добавляем статическое поле для хранения текущего Stage

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void navigateToPage(String pageName) {
        try {
            HibbernateRunner.setRoot(pageName); // Передаем текущий Stage
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
