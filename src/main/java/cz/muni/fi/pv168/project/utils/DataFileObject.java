package cz.muni.fi.pv168.project.utils;

import cz.muni.fi.pv168.project.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold all table information for export and import
 */
public class DataFileObject {


    public List<Unit> unitList = new ArrayList<>();
    public List<BaseUnit> baseUnitList = new ArrayList<>();
    public List<Category> categoryList = new ArrayList<>();
    public List<Ingredient> ingredientList = new ArrayList<>();
    public List<Recipe> recipeList = new ArrayList<>();

    public DataFileObject() {
    }

    public DataFileObject(List<Unit> unitList, List<BaseUnit> baseUnitList, List<Category> categoryList, List<Ingredient> ingredientList, List<Recipe> recipeList) {
        this.unitList = unitList;
        this.baseUnitList = baseUnitList;
        this.categoryList = categoryList;
        this.ingredientList = ingredientList;
        this.recipeList = recipeList;
    }

    public boolean isEmpty() {
        return unitList.size() == 0
                && categoryList.size() == 0
                && ingredientList.size() == 0
                && recipeList.size() == 0;
    }
}
