package cz.muni.fi.pv168.project.utils.exporters.formats;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.utils.DataFileObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JSONExporter implements FormattedExporter {

    private void exportUnits(JSONObject obj, List<Unit> units) {
        JSONArray unitArray = new JSONArray();
        for (int i = 0; i < units.size(); i++) {
            JSONObject unitObj = new JSONObject();
            Unit unit = units.get(i);
            unitObj.put("name", unit.getName());
            unitObj.put("abbreviation", unit.getAbbreviation());
            unitObj.put("conversionFactor", unit.getConversionFactor());
            int baseUnitIndex = -1;
            for (int j = 0; j < units.size(); j++) {
                Unit unitToCheck = units.get(j);
                if (unitToCheck.getName().equals(unit.getBaseUnit().getName())) {
                    baseUnitIndex = j;
                }
            }
            unitObj.put("baseUnit", baseUnitIndex);
            unitArray.add(unitObj);
        }
        obj.put("units", unitArray);
    }

    private void exportCategories(JSONObject obj, List<Category> categories) {
        JSONArray categoryArray = new JSONArray();
        for (int i = 0; i < categories.size(); i++) {
            JSONObject categoryObj = new JSONObject();
            Category category = categories.get(i);
            categoryObj.put("name", category.getName());
            categoryObj.put("description", category.getDescription());
            JSONArray colorArray = new JSONArray();
            colorArray.add(category.getColor().getRed());
            colorArray.add(category.getColor().getGreen());
            colorArray.add(category.getColor().getBlue());
            categoryObj.put("color", colorArray);
            categoryArray.add(categoryObj);
        }
        obj.put("categories", categoryArray);
    }

    private void exportIngredients(JSONObject obj, List<Ingredient> ingredients, List<Unit> units) {
        JSONArray ingredientArray = new JSONArray();
        for (int i = 0; i < ingredients.size(); i++) {
            JSONObject ingredientObj = new JSONObject();
            Ingredient ingredient = ingredients.get(i);
            ingredientObj.put("name", ingredient.getName());
            ingredientObj.put("nutritionalValue", ingredient.getNutritionValue());
            ingredientObj.put("unit", units.indexOf(ingredient.getUnit()));
            ingredientArray.add(ingredientObj);
        }
        obj.put("ingredients", ingredientArray);
    }

    private void exportRecipes(JSONObject obj, List<Recipe> recipes, List<Category> categories, List<Ingredient> ingredients) {
        JSONArray recipeArray = new JSONArray();
        for (int i = 0; i < recipes.size(); i++) {
            JSONObject recipeObj = new JSONObject();
            Recipe recipe = recipes.get(i);
            recipeObj.put("name", recipe.getName());
            recipeObj.put("description", recipe.getDescription());
            recipeObj.put("instructions", recipe.getInstructions());
            recipeObj.put("portions", recipe.getPortions());
            recipeObj.put("duration", recipe.getDuration());
            JSONArray ingredientsArray = new JSONArray();
            for (Map.Entry<Ingredient, Integer> ingredient : recipe.getIngredients().entrySet()) {
                JSONObject ingredientObj = new JSONObject();
                ingredientObj.put("order", ingredients.indexOf(ingredient.getKey()));
                ingredientObj.put("amount", ingredient.getValue());
                ingredientsArray.add(ingredientObj);
            }
            recipeObj.put("ingredients", ingredientsArray);
            recipeObj.put("category", categories.indexOf(recipe.getCategory()));
            recipeArray.add(recipeObj);
        }
        obj.put("recipes", recipeArray);
    }

    public void exportFile(String filePath, DataFileObject data) {
        JSONObject obj = new JSONObject();
        exportUnits(obj, data.unitList);
        exportCategories(obj, data.categoryList);
        exportIngredients(obj, data.ingredientList, data.unitList);
        exportRecipes(obj, data.recipeList, data.categoryList, data.ingredientList);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
