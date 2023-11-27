package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.persistance.dao.*;
import cz.muni.fi.pv168.project.persistance.db.DatabaseManager;
import cz.muni.fi.pv168.project.persistance.mapper.*;
import cz.muni.fi.pv168.project.persistance.repository.*;
import cz.muni.fi.pv168.project.persistance.validation.*;
import cz.muni.fi.pv168.project.utils.exporters.DaoExporter;
import cz.muni.fi.pv168.project.utils.exporters.Exporter;
import cz.muni.fi.pv168.project.utils.exporters.formats.*;
import cz.muni.fi.pv168.project.utils.importers.DaoImporter;
import cz.muni.fi.pv168.project.utils.importers.Importer;
import cz.muni.fi.pv168.project.utils.importers.formats.FormattedImporter;
import cz.muni.fi.pv168.project.utils.importers.formats.ImporterFormatType;
import cz.muni.fi.pv168.project.utils.importers.formats.JSONImporter;
import cz.muni.fi.pv168.project.utils.importers.formats.XMLImporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dependency provider common for all environments
 */
public abstract class CommonDependencyProvider implements DependencyProvider {
    private final Repository<Category> categories;
    private final Repository<Ingredient> ingredients;
    private final Repository<Unit> units;
    private final Repository<BaseUnit> baseUnits;
    private final Repository<Recipe> recipes;
    private Exporter exporter;
    private Importer importer;
    private final DatabaseManager databaseManager;
    private final Validator<Category> categoryValidator = new CategoryValidator();
    private final Validator<Ingredient> ingredientValidator = new IngredientValidator();
    private final Validator<Unit> unitValidator = new UnitValidator();
    private final Validator<Recipe> recipeValidator = new RecipeValidator();
    private final Validator<BaseUnit> baseUnitValidator = new BaseUnitValidator();
    protected CommonDependencyProvider(DatabaseManager databaseManager) {

        this.categories = new CategoryRepository(
                new CategoryDao(databaseManager::getConnectionHandler),
                new CategoryMapper(categoryValidator)
        );


        this.baseUnits = new BaseUnitRepository(
                new BaseUnitDao(databaseManager::getConnectionHandler),
                new BaseUnitMapper(baseUnitValidator)
        );

        this.units = new UnitRepository(
                new UnitDao(databaseManager::getConnectionHandler),
                new UnitMapper(baseUnits, unitValidator)
        );

        this.ingredients = new IngredientRepository(
                new IngredientDao(databaseManager::getConnectionHandler),
                new IngredientMapper(units, ingredientValidator)
        );

        this.recipes = new RecipeRepository(
                new RecipeDao(databaseManager::getConnectionHandler),
                new RecipeMapper(ingredients, categories, recipeValidator)
        );



        this.databaseManager = databaseManager;
    }

    public Validator<Category> getCategoryValidator() {
        return categoryValidator;
    }

    public Validator<Ingredient> getIngredientValidator() {
        return ingredientValidator;
    }

    public Validator<Unit> getUnitValidator() {
        return unitValidator;
    }

    public Validator<Recipe> getRecipeValidator() {
        return recipeValidator;
    }

    public Validator<BaseUnit> getBaseUnitValidator() {
        return baseUnitValidator;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return categories;
    }

    @Override
    public Repository<Ingredient> getIngredientRepository() {
        return ingredients;
    }

    @Override
    public Repository<Unit> getUnitRepository() {
        return units;
    }

    @Override
    public Repository<Recipe> getRecipeRepository() {
        return recipes;
    }

    @Override
    public Repository<BaseUnit> getBaseUnitRepository() {
        return baseUnits;
    }

    public DatabaseManager getCreatedDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Importer getImporter() {
        if (importer == null) {
            var database = getCreatedDatabaseManager();
            Map<ImporterFormatType, FormattedImporter> importers = new HashMap<>();

            importers.put(ImporterFormatType.JSON, new JSONImporter());
            importers.put(ImporterFormatType.XML, new XMLImporter());

            importer = new DaoImporter(
                    BaseUnitDao::new,
                    UnitDao::new,
                    CategoryDao::new,
                    IngredientDao::new,
                    RecipeDao::new,
                    database::getTransactionHandler,
                    getBaseUnitValidator(),
                    getUnitValidator(),
                    getCategoryValidator(),
                    getIngredientValidator(),
                    getRecipeValidator(),
                    importers
            );
        }
        return importer;
    }

    @Override
    public Exporter getExporter() {
        if (exporter == null) {

            var database = getCreatedDatabaseManager();
            Map<ExporterFormatType, FormattedExporter> exporters = new HashMap<>();

            exporters.put(ExporterFormatType.JSON, new JSONExporter());
            exporters.put(ExporterFormatType.XML, new XMLExporter());
            exporters.put(ExporterFormatType.PDF, new PDFExporter());

            exporter = new DaoExporter(
                    BaseUnitDao::new,
                    UnitDao::new,
                    CategoryDao::new,
                    IngredientDao::new,
                    RecipeDao::new,
                    database::getTransactionHandler,
                    exporters
            );

        }
        return exporter;
    }

    public void refresh() {
        categories.refresh();
        baseUnits.refresh();
        units.refresh();
        ingredients.refresh();
        recipes.refresh();
    }
}
