package cz.muni.fi.pv168.project.wiring;


import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.persistance.db.DatabaseManager;
import cz.muni.fi.pv168.project.persistance.repository.Repository;
import cz.muni.fi.pv168.project.utils.exporters.Exporter;
import cz.muni.fi.pv168.project.utils.importers.Importer;


/**
 * Dependency provider interface
 */
public interface DependencyProvider {
    Repository<Category> getCategoryRepository();

    Repository<Ingredient> getIngredientRepository();

    Repository<Unit> getUnitRepository();

    Repository<Recipe> getRecipeRepository();

    Repository<BaseUnit> getBaseUnitRepository();

    DatabaseManager getCreatedDatabaseManager();

    Importer getImporter();

    Exporter getExporter();

    void refresh();
}
