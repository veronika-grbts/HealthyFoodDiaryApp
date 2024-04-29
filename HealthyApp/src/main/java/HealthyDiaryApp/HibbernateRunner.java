package HealthyDiaryApp;

import HealthyDiaryApp.navigation.NavigationMenu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;

/*
 * LoseWeightCalculator class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Запуск додатку НealthyDiary.
 */

@Slf4j
public class HibbernateRunner extends Application {
    private static Stage stage;
    private static double windowWidth = 1360; // Значения по умолчанию
    private static double windowHeight = 720; // Значения по умолчанию
    private static double windowX = 0; // Значения по умолчанию
    private static double windowY = 0; // Значения по умолчанию
    private static boolean maximized = false; // Переменная для хранения состояния максимизации окна

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        NavigationMenu.setStage(primaryStage);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOnCloseRequest(event -> saveWindowSizeAndPosition());
        restoreWindowSizeAndPosition(); // Восстановление размеров и положения окна
        stage.show();
    }

    private static void saveWindowSizeAndPosition() {
        windowWidth = stage.getWidth();
        windowHeight = stage.getHeight();
        windowX = stage.getX();
        windowY = stage.getY();
        maximized = stage.isMaximized(); // Сохраняем состояние максимизации
    }

    private static void restoreWindowSizeAndPosition() {
        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);
        stage.setX(windowX);
        stage.setY(windowY);
        if (maximized) {
            stage.setMaximized(true); // Если окно было максимизировано, восстанавливаем максимизацию
        }
    }

    public static void setRoot(Stage stage, String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HibbernateRunner.class.getResource("/fxml/" + fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
    }



    public static void main(String[] args) {
        launch();
    }
}

/*


public class HibbernateRunner extends Application {

    private static Stage stage;
    private static  double xOffset = 0;
    private static  double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        setRoot("primary");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HibbernateRunner.class.getResource("/fxml/" + fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.setScene(scene);
        stage.sizeToScene();
    }

    public static void main(String[] args) {
        launch();
    }
}*/
