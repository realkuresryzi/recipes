package cz.muni.fi.pv168.project.utils.exporters.formats;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.utils.DataFileObject;

import java.util.List;


public interface FormattedExporter {

//    R exportUnits(T obj, List<Unit> units);
//
//    R exportCategories(T obj, List<Category> categories);
//
//    R exportIngredients(T obj, List<Ingredient> ingredients, List<Unit> units);
//
//    R exportRecipes(T obj, List<Recipe> recipes, List<Category> categories, List<Ingredient> ingredients);

    void exportFile(String filePath, DataFileObject data);
}
