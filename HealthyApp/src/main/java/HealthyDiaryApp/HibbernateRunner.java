package HealthyDiaryApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        String fxmlPath = "file:///C:/Users/User/IdeaProjects/HealthyApp/src/main/resources/fxml/primary.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(new URL(fxmlPath));
        Parent root = fxmlLoader.load();
        scene = new Scene(root); // изменение здесь
        stage.setScene(scene);
        stage.show();
    }


    public static void setRoot(String fxml) throws IOException {
        String path = "/fxml/" + fxml + ".fxml";
        URL resourceUrl = HibbernateRunner.class.getResource(path);
        if (resourceUrl == null) {
            throw new IOException("FXML file not found: " + path);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
        Parent root = fxmlLoader.load();
        scene.setRoot(root);
    }

    public static void main(String[] args) {
        launch();
    }
}