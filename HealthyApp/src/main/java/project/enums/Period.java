package project.enums;

public enum Period {
    ONE_DAY("Один день", 1),
    THREE_DAYS("Три дні", 3),
    WEEK("Тиждень", 7),
    TWO_WEEKS("Два тижні", 14);

    private final String label;
    private final int days;

        Period(String label, int days) {
        this.label = label;
        this.days = days;
        }

    public String getLabel() {
        return label;
        }

    public int getDays() {
        return days;
        }
}
