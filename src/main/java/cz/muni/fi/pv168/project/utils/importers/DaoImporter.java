package cz.muni.fi.pv168.project.utils.importers;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.persistance.dao.DaoSupplier;
import cz.muni.fi.pv168.project.persistance.dao.DataAccessObject;
import cz.muni.fi.pv168.project.persistance.db.TransactionHandler;
import cz.muni.fi.pv168.project.persistance.entity.*;
import cz.muni.fi.pv168.project.persistance.mapper.CategoryMapper;
import cz.muni.fi.pv168.project.persistance.mapper.IngredientMapper;
import cz.muni.fi.pv168.project.persistance.mapper.RecipeMapper;
import cz.muni.fi.pv168.project.persistance.mapper.UnitMapper;
import cz.muni.fi.pv168.project.persistance.validation.Validator;
import cz.muni.fi.pv168.project.utils.DataFileObject;
import cz.muni.fi.pv168.project.utils.importers.formats.FormattedImporter;
import cz.muni.fi.pv168.project.utils.importers.formats.ImporterFormatType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class DaoImporter implements Importer {

    private final DaoSupplier<BaseUnitEntity> baseUnitDaoSupplier;
    private final DaoSupplier<UnitEntity> unitEntityDaoSupplier;
    private final DaoSupplier<CategoryEntity> categoryEntityDaoSupplier;
    private final DaoSupplier<IngredientEntity> ingredientEntityDaoSupplier;
    private final DaoSupplier<RecipeEntity> recipeEntityDaoSupplier;
    private final Supplier<TransactionHandler> transactionHandlerSupplier;
    private final Validator<BaseUnit> baseUnitValidator;
    private final Validator<Unit> unitValidator;
    private final Validator<Category> categoryValidator;
    private final Validator<Ingredient> ingredientValidator;
    private final Validator<Recipe> recipeValidator;
    private final Map<ImporterFormatType, FormattedImporter> importers;
    public DaoImporter(DaoSupplier<BaseUnitEntity> baseUnitDaoSupplier,
                       DaoSupplier<UnitEntity> unitEntityDaoSupplier,
                       DaoSupplier<CategoryEntity> categoryEntityDaoSupplier,
                       DaoSupplier<IngredientEntity> ingredientEntityDaoSupplier,
                       DaoSupplier<RecipeEntity> recipeEntityDaoSupplier,
                       Supplier<TransactionHandler> transactionHandlerSupplier,
                       Validator<BaseUnit> baseUnitValidator,
                       Validator<Unit> unitValidator,
                       Validator<Category> categoryValidator,
                       Validator<Ingredient> ingredientValidator,
                       Validator<Recipe> recipeValidator,
                       Map<ImporterFormatType, FormattedImporter> importers) {
        this.baseUnitDaoSupplier = baseUnitDaoSupplier;
        this.unitEntityDaoSupplier = unitEntityDaoSupplier;
        this.categoryEntityDaoSupplier = categoryEntityDaoSupplier;
        this.ingredientEntityDaoSupplier = ingredientEntityDaoSupplier;
        this.recipeEntityDaoSupplier = recipeEntityDaoSupplier;
        this.transactionHandlerSupplier = transactionHandlerSupplier;
        this.baseUnitValidator = baseUnitValidator;
        this.unitValidator = unitValidator;
        this.categoryValidator = categoryValidator;
        this.ingredientValidator = ingredientValidator;
        this.recipeValidator = recipeValidator;
        this.importers = importers;
    }

    @Override
    public void importFile(String filePath, ImporterFormatType formatType) {
        try (var tx = transactionHandlerSupplier.get()) {
            List<BaseUnit> baseUnitList = new ArrayList<>();
            for (var baseUnit : baseUnitDaoSupplier.get(tx::connection).findAll()) {
                baseUnitList.add(new BaseUnit(
                        baseUnit.id(),
                        baseUnit.name(),
                        baseUnit.abbreviation()
                ));
            }

            DataFileObject data = importers.get(formatType).importFile(filePath, baseUnitList);

            DataAccessObject<UnitEntity> unitDao = unitEntityDaoSupplier.get(tx::connection);
            DataAccessObject<CategoryEntity> categoryDao = categoryEntityDaoSupplier.get(tx::connection);
            DataAccessObject<IngredientEntity> ingredientDao = ingredientEntityDaoSupplier.get(tx::connection);
            DataAccessObject<RecipeEntity> recipeDao = recipeEntityDaoSupplier.get(tx::connection);

            recipeDao.deleteAll();
            ingredientDao.deleteAll();
            categoryDao.deleteAll();
            unitDao.deleteAll();

            CategoryMapper categoryMapper = new CategoryMapper(categoryValidator);
            data.categoryList.forEach(cat -> {
                categoryValidator.validate(cat).intoException();
                var entity = categoryDao.create(categoryMapper.mapToEntity(cat));
                cat.setId(entity.id());
            });

            UnitMapper unitMapper = new UnitMapper(unitValidator, id -> Optional.empty());
            data.unitList.forEach(unit -> {
                unitValidator.validate(unit).intoException();
                var entity = unitDao.create(unitMapper.mapToEntity(unit));
                unit.setId(entity.id());
            });

            IngredientMapper ingredientMapper = new IngredientMapper(id -> Optional.empty(), ingredientValidator);
            data.ingredientList.forEach(ing -> {
                ingredientValidator.validate(ing).intoException();
                var entity = ingredientDao.create(ingredientMapper.mapToEntity(ing));
                ing.setId(entity.id());
            });

            RecipeMapper recipeMapper = new RecipeMapper(id -> Optional.empty(), id -> Optional.empty(), recipeValidator);
            data.recipeList.stream()
                    .peek(rec -> recipeValidator.validate(rec).intoException())
                    .map(recipeMapper::mapToEntity)
                    .forEach(recipeDao::create);

            tx.commit();
        }
    }
}
