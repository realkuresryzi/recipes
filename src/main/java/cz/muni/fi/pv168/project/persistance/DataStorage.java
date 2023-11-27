package cz.muni.fi.pv168.project.persistance;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;

import java.util.List;

public class DataStorage {

    private List<Recipe> recipes;
    private List<Ingredient> ingredients;
    private List<Category> catergories;

    public DataStorage() {
        //this.recipes = Recipe.getTestRecipes();
        //this.catergories = Category.getTestCategories();
        this.ingredients = Ingredient.getTestIngredients();
    }

}
