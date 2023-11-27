package cz.muni.fi.pv168.project.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Recipe {
    private static Long ID = new Long(0);
    private Long id;
    private String name;
    private String description;
    private Map<Ingredient, Integer> ingredients;
    private String instructions;
    private Category category;
    private int portions;
    private int duration;

    public Recipe(String name, String description, Map<Ingredient, Integer> ingredients, String instructions, Category category, int portions, int duration) {
        this.id = ++ID;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.category = category;
        this.portions = portions;
        this.duration = duration;
    }

    public Recipe(Long id, String name, String description, Map<Ingredient, Integer> ingredients, String instructions, Category category, int portions, int duration) {
        ID = id;
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.category = category;
        this.portions = portions;
        this.duration = duration;
    }

    public Recipe() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public static List<Recipe> getTestRecipes() {
        List<Ingredient> ingredients = Ingredient.getTestIngredients();
        List<Category> categories = Category.getTestCategories();
        List<Recipe> recipes = List.of(
                new Recipe("Milkshake", "The best milkshake you ever had", Map.of(ingredients.get(0), 200, ingredients.get(3), 5, ingredients.get(4), 30), "Put everything into the blender and mix", categories.get(2), 2, 10)
                , new Recipe("Scrambled eggs", "Scrambled eggs as you know them", Map.of(ingredients.get(1), 3, ingredients.get(5), 2, ingredients.get(6), 20, ingredients.get(7), 1), "Stir everything in the pan and eat with the bread", categories.get(4), 1, 20)
                , new Recipe("Toast and egg", "Toast with butter and egg", Map.of(ingredients.get(5), 2, ingredients.get(6), 1, ingredients.get(1), 1), "Apply butter on toast and place in toaster butter side up (so butter touches toaster). Only cook for about 2-3 minutes and check. Also while toast is in toaste place an egg to a heated pan at medium heat. Cook the egg for abotu 6 minutes so double the toaster.", categories.get(5), 1, 6)

        );
        return recipes;
    }


    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", instructions='" + instructions + '\'' +
                ", category=" + category +
                ", portions=" + portions +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return name.equals(recipe.name)
                && description.equals(recipe.description)
                && Objects.equals(ingredients, recipe.ingredients)
                && instructions.equals(recipe.instructions)
                && category.equals(recipe.category)
                && portions == recipe.portions
                && duration == recipe.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, ingredients, instructions, category, portions, duration);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
