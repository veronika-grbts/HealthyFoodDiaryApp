package HealthyDiaryApp;

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
}
