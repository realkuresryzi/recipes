package cz.muni.fi.pv168.project.utils.importers.formats;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.utils.DataFileObject;

import java.util.List;


public interface FormattedImporter {

//    UnitListWrapper parseUnits(T units);
//
//    List<Category> parseCategories(T categories);
//
//    List<Ingredient> parseIngredients(T ingredients, List<Unit> unitList);
//
//    List<Recipe> parseRecipes(T recipes, List<Ingredient> ingredientList, List<Category> categoryList);

    DataFileObject importFile(String fileContents, List<BaseUnit> baseUnits);
}
