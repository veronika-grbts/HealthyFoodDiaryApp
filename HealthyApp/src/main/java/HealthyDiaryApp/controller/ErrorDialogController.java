package HealthyDiaryApp.controller;

import HealthyDiaryApp.view.ErrorDialogView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class ErrorDialogController {

    public static void showErrorAlert(String title, String message) {
        try {
            String fxmlPath = "file:///C:/Users/User/IdeaProjects/HealthyApp/src/main/resources/fxml/errorDialog.fxml";
            // Загружаем FXML для диалогового окна ошибки
            FXMLLoader loader = new FXMLLoader(new URL(fxmlPath));
            Parent root = loader.load();

            // Создаем новое окно
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));

            // Устанавливаем модальное окно (блокирующее)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Устанавливаем сообщение об ошибке
            ErrorDialogView controller = loader.getController();
            controller.setMessage(message);
            controller.setTitleMessager(title);
            // Показываем окно
            stage.showAndWait();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
