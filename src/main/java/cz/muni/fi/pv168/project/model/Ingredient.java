package cz.muni.fi.pv168.project.model;

import java.util.List;
import java.util.Objects;

public class Ingredient {
    private static Long ID = Long.valueOf(0);
    private Long id;
    private String name;
    private Unit unit;
    private int nutritionValue;

    public Ingredient(String name, Unit unit, int nutritionValue) {
        id = ++ID;
        this.name = name;
        this.unit = unit;
        this.nutritionValue = nutritionValue;
    }

    public Ingredient(Long id, String name, Unit unit, int nutritionValue) {
        ID = id;
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.nutritionValue = nutritionValue;
    }

    public Ingredient() {
    }

    public static List<Ingredient> getTestIngredients() {
        List<Unit> units = Unit.getTestUnits();


        List<Ingredient> ingredients = List.of(
                new Ingredient("Milk", units.get(0), 100),
                new Ingredient("Egg", units.get(7), 200),
                new Ingredient("Flour", units.get(2), 300),
                new Ingredient("Strawberry", units.get(7), 20),
                new Ingredient("Sugar", units.get(2), 15),
                new Ingredient("Slice of bread", units.get(7), 200),
                new Ingredient("Butter", units.get(2), 200),
                new Ingredient("Onion", units.get(7), 50)
        );
        return ingredients;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public int getNutritionValue() {
        return nutritionValue;
    }

    public void setNutritionValue(int nutritionValue) {
        this.nutritionValue = nutritionValue;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return nutritionValue == that.nutritionValue
                && name.equals(that.name)
                && unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unit, nutritionValue);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
