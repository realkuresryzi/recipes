package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.persistance.db.DatabaseManager;

/**
 * Dependency provider for production environment
 */
public final class ProductionDependencyProvider extends CommonDependencyProvider {

    public ProductionDependencyProvider(boolean drop) {
        super(getDatabaseManager(drop));
    }

    private static DatabaseManager getDatabaseManager(boolean drop) {
        DatabaseManager databaseManager = DatabaseManager.createProductionInstance();
        if (drop) {
            databaseManager.destroySchema();
        }
        databaseManager.initSchema();

        return databaseManager;
    }
}
