package HealthyDiaryApp.navigation;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class MouseEnterHandler {
    public static void addMouseEnterHandler(Button loseWeightMenuButton, Button forecastMenuItem, Button progresisMenuItem, Button changePageBtn) {

        loseWeightMenuButton.setOnMouseEntered(event -> {
            // Показать forecastMenuItem с анимацией
            forecastMenuItem.setVisible(true);
            fadeInButton(forecastMenuItem);

            // Показать progresisMenuItem с анимацией
            progresisMenuItem.setVisible(true);
            fadeInButton(progresisMenuItem);

            // Сместить changePageBtn вниз на расстояние, равное высоте forecastMenuItem и progresisMenuItem
            double totalHeight = forecastMenuItem.getHeight() + progresisMenuItem.getHeight();
            changePageBtn.setLayoutY(loseWeightMenuButton.getLayoutY() + loseWeightMenuButton.getHeight() + totalHeight);

            progresisMenuItem.setVisible(true);
            forecastMenuItem.setVisible(true);

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), changePageBtn);
            transition.setToY(15); // Используем оригинальное значение + смещение
            transition.play();
        });

        loseWeightMenuButton.setOnMouseExited(event -> {
            // Запустить таймер на 2 секунды и скрыть progresisMenuItem и forecastMenuItem после истечения времени
            Timer timer = new Timer();
            final long DELAY = 2000; // 2 секунды
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        progresisMenuItem.setVisible(false);
                        forecastMenuItem.setVisible(false);
                        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), changePageBtn);
                        transition.setToY(-106); // Используем оригинальное значение
                        transition.play();
                    });
                }
            }, DELAY);
        });
    }

    private static void fadeInButton(Button button) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), button);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }
}
