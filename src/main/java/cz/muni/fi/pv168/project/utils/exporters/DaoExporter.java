package cz.muni.fi.pv168.project.utils.exporters;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.persistance.dao.DaoSupplier;
import cz.muni.fi.pv168.project.persistance.db.TransactionHandler;
import cz.muni.fi.pv168.project.persistance.entity.*;
import cz.muni.fi.pv168.project.utils.DataFileObject;
import cz.muni.fi.pv168.project.utils.FormattedOperationException;
import cz.muni.fi.pv168.project.utils.exporters.formats.ExporterFormatType;
import cz.muni.fi.pv168.project.utils.exporters.formats.FormattedExporter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DaoExporter implements Exporter {
    private final DaoSupplier<BaseUnitEntity> baseUnitDaoSupplier;
    private final DaoSupplier<UnitEntity> unitEntityDaoSupplier;
    private final DaoSupplier<CategoryEntity> categoryEntityDaoSupplier;
    private final DaoSupplier<IngredientEntity> ingredientEntityDaoSupplier;
    private final DaoSupplier<RecipeEntity> recipeEntityDaoSupplier;
    private final Supplier<TransactionHandler> transactionHandlerSupplier;
    private final Map<ExporterFormatType, FormattedExporter> exporters;

    public DaoExporter(DaoSupplier<BaseUnitEntity> baseUnitDaoSupplier,
                       DaoSupplier<UnitEntity> unitEntityDaoSupplier,
                       DaoSupplier<CategoryEntity> categoryEntityDaoSupplier,
                       DaoSupplier<IngredientEntity> ingredientEntityDaoSupplier,
                       DaoSupplier<RecipeEntity> recipeEntityDaoSupplier,
                       Supplier<TransactionHandler> transactionHandlerSupplier,
                       Map<ExporterFormatType, FormattedExporter> exporters) {
        this.baseUnitDaoSupplier = baseUnitDaoSupplier;
        this.unitEntityDaoSupplier = unitEntityDaoSupplier;
        this.categoryEntityDaoSupplier = categoryEntityDaoSupplier;
        this.ingredientEntityDaoSupplier = ingredientEntityDaoSupplier;
        this.recipeEntityDaoSupplier = recipeEntityDaoSupplier;
        this.transactionHandlerSupplier = transactionHandlerSupplier;
        this.exporters = exporters;
    }

    @Override
    public void exportFile(String filePath, ExporterFormatType formatType) {
        FormattedExporter exporter = exporters.get(formatType);

        try (var tx = transactionHandlerSupplier.get()) {
            List<BaseUnit> baseUnitList = new ArrayList<>();
            for (var baseUnit : baseUnitDaoSupplier.get(tx::connection).findAll()) {
                baseUnitList.add(new BaseUnit(
                        baseUnit.id(),
                        baseUnit.name(),
                        baseUnit.abbreviation()
                ));
            }

            List<Unit> unitList = new ArrayList<>();
            for (var unit : unitEntityDaoSupplier.get(tx::connection).findAll()) {
                unitList.add(new Unit(
                        unit.id(),
                        unit.name(),
                        unit.abbreviation(),
                        unit.conversionFactor(),
                        baseUnitList.get(unit.baseUnitID().intValue() - 1)
                ));
            }

            List<Category> categoryList = new ArrayList<>();
            for (var category : categoryEntityDaoSupplier.get(tx::connection).findAll()) {
                categoryList.add(new Category(
                        category.id(),
                        category.name(),
                        category.description(),
                        category.color()
                ));
            }

            List<Ingredient> ingredientList = new ArrayList<>();
            for (var ingredient : ingredientEntityDaoSupplier.get(tx::connection).findAll()) {
                ingredientList.add(new Ingredient(
                        ingredient.id(),
                        ingredient.name(),
                        unitList.get(ingredient.unitId().intValue() - 1),
                        ingredient.nutritionValue()
                ));
            }



            List<Recipe> recipeList = new ArrayList<>();
            for (var recipe : recipeEntityDaoSupplier.get(tx::connection).findAll()) {
                Map<Ingredient, Integer> recipeIngredientList = new HashMap<>();
                for (Map.Entry<Long, Integer> entry : recipe.ingredientsAmounts().entrySet()) {
                    recipeIngredientList.put(ingredientList.get(entry.getKey().intValue() - 1), entry.getValue());
                }
                recipeList.add(new Recipe(
                        recipe.id(),
                        recipe.name(),
                        recipe.description(),
                        recipeIngredientList,
                        recipe.instructions(),
                        categoryList.get(recipe.categoryId().intValue() - 1),
                        recipe.portions(),
                        recipe.duration()
                ));
            }

            var data = new DataFileObject(unitList, baseUnitList, categoryList, ingredientList, recipeList);

            try {
                exporter.exportFile(filePath, data);
            } catch (RuntimeException ex) {
                throw new FormattedOperationException("Export failed", ex);
            }
        }
    }

}
