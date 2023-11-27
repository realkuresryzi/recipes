package cz.muni.fi.pv168.project.persistance.entity;

/**
 * Represents the Ingredient entity
 *
 * @param id             Primary key in the database (sequential)
 * @param name           Name of the ingredient
 * @param unitId         Unit ID of the ingredient
 * @param nutritionValue Nutrition value of the ingredient in kcal
 */
public record IngredientEntity(
        Long id,
        String name,
        Long unitId,
        int nutritionValue
) {
    public IngredientEntity(
            String name,
            Long unitId,
            int nutritionValue
    ) {
        this(null, name, unitId, nutritionValue);
    }
}
