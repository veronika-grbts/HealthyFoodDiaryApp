package project.method;

import java.util.Objects;

public class Nutrition {
    private double protein;
    private double fat;
    private double carbohydrates;

    public Nutrition(double protein, double fat, double carbohydrates) {
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    // Метод для добавления питательной ценности из другого объекта Nutrition
    public void addNutrition(Nutrition other) {
        this.protein += other.getProtein();
        this.fat += other.getFat();
        this.carbohydrates += other.getCarbohydrates();
    }

    public void subtractNutrition(Nutrition other) {
        this.protein -= other.protein;
        this.fat -= other.fat;
        this.carbohydrates -= other.carbohydrates;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Nutrition nutrition = (Nutrition) obj;
        return Double.compare(nutrition.protein, protein) == 0 &&
                Double.compare(nutrition.fat, fat) == 0 &&
                Double.compare(nutrition.carbohydrates, carbohydrates) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(protein, fat, carbohydrates);
    }

    @Override
    public String toString() {
        return "Nutrition{" +
                "protein=" + protein +
                ", fat=" + fat +
                ", carbohydrates=" + carbohydrates +
                '}';
    }

}

