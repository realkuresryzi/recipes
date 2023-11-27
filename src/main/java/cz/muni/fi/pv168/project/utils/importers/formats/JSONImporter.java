package cz.muni.fi.pv168.project.utils.importers.formats;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.utils.DataFileObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;

/**
 * Class encapsulating all functions for JSON import and export.
 */
public class JSONImporter implements FormattedImporter {

    private List<Unit> parseUnits(JSONArray units, List<BaseUnit> baseUnits) {
        List<Unit> unitList = new ArrayList<>();
        Iterator it = units.iterator();
        while (it.hasNext()) {
            JSONObject obj = (JSONObject) it.next();
            unitList.add(new Unit(
                    (String) obj.get("name"),
                    (String) obj.get("abbreviation"),
                    (Long) obj.get("conversionFactor"),
                    baseUnits.get(((Long) obj.get("baseUnit")).intValue())
            ));
        }
        return unitList;
    }

    private List<Category> parseCategories(JSONArray categories) {
        List<Category> categoryList = new ArrayList<>();
        Iterator it = categories.iterator();
        while (it.hasNext()) {
            JSONObject obj = (JSONObject) it.next();
            Object[] colorsArray = ((JSONArray) obj.get("color")).toArray();
            categoryList.add(new Category(
                    1L,
                    (String) obj.get("name"),
                    (String) obj.get("description"),
                    new Color(((Long) colorsArray[0]).intValue(), ((Long) colorsArray[1]).intValue(), ((Long) colorsArray[2]).intValue())
            ));
        }
        return categoryList;
    }

    private List<Ingredient> parseIngredients(JSONArray ingredients, List<Unit> unitList) {
        List<Ingredient> ingredientList = new ArrayList<>();
        Iterator it = ingredients.iterator();
        while (it.hasNext()) {
            JSONObject obj = (JSONObject) it.next();
            ingredientList.add(new Ingredient(
                    (String) obj.get("name"),
                    unitList.get(((Long) obj.get("unit")).intValue()),
                    ((Long) obj.get("nutritionalValue")).intValue()
            ));
        }
        return ingredientList;
    }

    private List<Recipe> parseRecipes(JSONArray recipes, List<Ingredient> ingredientList, List<Category> categoryList) {
        List<Recipe> recipeList = new ArrayList<>();
        Iterator it = recipes.iterator();
        while (it.hasNext()) {
            JSONObject obj = (JSONObject) it.next();
            Map<Ingredient, Integer> ingredients = new HashMap<>();
            JSONArray ingredientsArrayObj = (JSONArray) obj.get("ingredients");
            Iterator ingredientsIt = ingredientsArrayObj.iterator();
            while (ingredientsIt.hasNext()) {
                JSONObject ingredient = (JSONObject) ingredientsIt.next();
                ingredients.put(ingredientList.get(((Long) ingredient.get("order")).intValue()), ((Long) ingredient.get("amount")).intValue());
            }
            recipeList.add(new Recipe(
                    (String) obj.get("name"),
                    (String) obj.get("description"),
                    ingredients,
                    (String) obj.get("instructions"),
                    categoryList.get(((Long) obj.get("category")).intValue()),
                    ((Long) obj.get("portions")).intValue(),
                    ((Long) obj.get("duration")).intValue()
            ));
        }
        return recipeList;
    }

    public DataFileObject importFile(String filePath, List<BaseUnit> baseUnits) {
        JSONObject obj = null;
        try {
            obj = (JSONObject) JSONValue.parse(Files.readString(Path.of(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DataFileObject importedData = new DataFileObject();
        importedData.unitList = parseUnits((JSONArray) obj.get("units"), baseUnits);
        importedData.categoryList = parseCategories((JSONArray) obj.get("categories"));
        importedData.ingredientList = parseIngredients((JSONArray) obj.get("ingredients"), importedData.unitList);
        importedData.recipeList = parseRecipes((JSONArray) obj.get("recipes"), importedData.ingredientList, importedData.categoryList);

        return importedData;
    }

}
