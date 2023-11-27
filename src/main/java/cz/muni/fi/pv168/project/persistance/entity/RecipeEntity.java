package cz.muni.fi.pv168.project.persistance.entity;

import java.util.Map;

/**
 * Represents the Recipe entity
 *
 * @param id                 Primary key in the database (sequential)
 * @param name               Name of the recipe
 * @param description        Description of the recipe
 * @param instructions       Instructions of the recipe
 * @param ingredientsAmounts Ingredients IDs and their amounts in the recipe
 * @param categoryId         Category Id of the recipe
 * @param portions           Portions of the recipe
 * @param duration           Preparation time
 */
public record RecipeEntity(
        Long id,
        String name,
        String description,
        String instructions,
        Map<Long, Integer> ingredientsAmounts,
        Long categoryId,
        int portions,
        int duration
) {
    public RecipeEntity(
            String name,
            String description,
            String instructions,
            Map<Long, Integer> ingredientsAmounts,
            Long categoryId,
            int portions,
            int duration
    ) {
        this(null, name, description, instructions, ingredientsAmounts, categoryId, portions, duration);
    }
}
