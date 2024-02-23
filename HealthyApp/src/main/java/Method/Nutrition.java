package Method;

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

    public double getProteinPercentage() {
        double total = protein + fat + carbohydrates;
        return protein / total * 100;
    }

    public double getFatPercentage() {
        double total = protein + fat + carbohydrates;
        return fat / total * 100;
    }

    public double getCarbohydratesPercentage() {
        double total = protein + fat + carbohydrates;
        return carbohydrates / total * 100;
    }

}
