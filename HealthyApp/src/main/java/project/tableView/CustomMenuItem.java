package project.tableView;
import project.entity.UserSelectedMenu;

public class CustomMenuItem extends UserSelectedMenu {
    private String mealType;
    private String name;
    private double quantity;
    private boolean isDayHeader;

    public CustomMenuItem(String mealType, String name, double quantity) {
        this.mealType = mealType;
        this.name = name;
        this.quantity = quantity;

    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public boolean isLastMenuOfDay() {
        return "Напій для вічері".equals(mealType);
    }

    public boolean isDayHeader() {
        return isDayHeader;
    }

    public void setDayHeader(boolean dayHeader) {
        isDayHeader = dayHeader;
    }
}
