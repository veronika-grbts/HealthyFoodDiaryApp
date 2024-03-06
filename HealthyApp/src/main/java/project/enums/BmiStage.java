package project.enums;

public enum BmiStage {
    UNDERWEIGHT("Недостатня вага", "Ваш ІМТ менше 18.5. Рекомендується звернутися до лікаря для оцінки стану здоров'я та харчування."),
    NORMAL_WEIGHT("Нормальна вага", "Ваш ІМТ знаходиться у діапазоні від 18.5 до 24.9. Вітаємо! Ваша вага вважається здоровою."),
    OVERWEIGHT("Зайва вага", "Ваш ІМТ знаходиться у діапазоні від 25 до 29.9. Рекомендується контролювати харчування та збільшити фізичну активність."),
    OBESITY("Ожиріння", "Ваш ІМТ 30 або більше. Рекомендується звернутися до лікаря для розробки плану лікування та зміни способу життя.");

    private final String label;
    private final String description;

    BmiStage(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
