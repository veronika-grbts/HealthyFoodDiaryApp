package HealthyDiaryApp.view;

import javafx.stage.Stage;

public class WindowSize {
    private static double width;
    private static double height;

    public static void saveWindowSize(Stage stage) {
        width = stage.getWidth();
        height = stage.getHeight();
    }

    public static void restoreWindowSize(Stage stage) {
        if (width > 0 && height > 0) {
            stage.setWidth(width);
            stage.setHeight(height);
        }
    }

    // Метод для сохранения новых размеров окна
    public static void saveNewWindowSize(double newWidth, double newHeight) {
        width = newWidth;
        height = newHeight;
    }
}
