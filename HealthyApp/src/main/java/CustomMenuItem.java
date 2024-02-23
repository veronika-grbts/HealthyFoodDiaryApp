import entity.UserSelectedMenu;

public class CustomMenuItem extends UserSelectedMenu {
    private String mealType;
    private String name;
    private double quantity;

    public CustomMenuItem(String mealType, String name, Double quantity) {
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
}
