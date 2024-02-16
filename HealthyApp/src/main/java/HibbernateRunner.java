import entity.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HibbernateRunner extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HibbernateRunner.class.getResource("primary.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root);

        // Устанавливаем сцену
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml, User user) throws IOException {
        URL resourceUrl = HibbernateRunner.class.getResource(fxml + ".fxml");
        if (resourceUrl == null) {
            throw new IOException("FXML file not found: " + fxml + ".fxml");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
        Parent root = fxmlLoader.load();

        if (user != null && fxml.equals("mainpage")) {
            MainPageController mainPageController = fxmlLoader.getController();
            mainPageController.fillUserData(user);
        }

        scene.setRoot(root);
    }

    public static void main(String[] args) {
        launch();
    }
}