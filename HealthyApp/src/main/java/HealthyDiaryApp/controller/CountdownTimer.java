package HealthyDiaryApp.controller;

import java.time.LocalDateTime;
import javafx.animation.AnimationTimer;
import java.time.Duration;
import java.time.LocalDateTime;

public class CountdownTimer {
    private LocalDateTime targetDateTime;

    public CountdownTimer(LocalDateTime targetDateTime) {
        this.targetDateTime = targetDateTime;
    }

    // Метод для получения компонентов времени (дни, часы, минуты, секунды)
    public String[] getTimeComponents() {
        Duration duration = Duration.between(LocalDateTime.now(), targetDateTime);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return new String[]{String.format("%02d", days), String.format("%02d", hours),
                String.format("%02d", minutes), String.format("%02d", seconds)};
    }

    // Метод для проверки, завершен ли таймер
    public boolean isTimerFinished() {
        return LocalDateTime.now().isAfter(targetDateTime);
    }

    public LocalDateTime getFinishDate() {
        return targetDateTime;
    }

}