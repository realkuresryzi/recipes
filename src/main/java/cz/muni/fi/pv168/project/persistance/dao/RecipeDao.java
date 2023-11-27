package cz.muni.fi.pv168.project.persistance.dao;

import cz.muni.fi.pv168.project.persistance.db.ConnectionHandler;
import cz.muni.fi.pv168.project.persistance.entity.RecipeEntity;
import cz.muni.fi.pv168.project.persistance.exception.DataStorageException;
import cz.muni.fi.pv168.project.ui.MainWindow;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Supplier;

public class RecipeDao implements DataAccessObject<RecipeEntity> {
    private final Supplier<ConnectionHandler> connections;

    public RecipeDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public RecipeEntity create(RecipeEntity entity) {
        String sql = "INSERT INTO  Recipe (name, description, instructions, categoryID, portions, duration) VALUES (?, ?, ?, ?, ?, ?);";

        String sqlIngredients = "INSERT INTO  IngredientRecipe (ingredientID, recipeID, amount) VALUES (?, ?, ?);";


        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                var connectionIngredients = connections.get();
                var statementIngredients = connectionIngredients.use().prepareStatement(sqlIngredients);
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.description());
            statement.setString(3, entity.instructions());
            statement.setLong(4, entity.categoryId());
            statement.setInt(5, entity.portions());
            statement.setInt(6, entity.duration());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long recipeId;

                if (keyResultSet.next()) {
                    recipeId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                statementIngredients.setLong(2, recipeId);
                for (var entry : entity.ingredientsAmounts().entrySet()) {
                    statementIngredients.setLong(1, entry.getKey());
                    statementIngredients.setInt(3, entry.getValue());

                    if (statementIngredients.executeUpdate() == 0) {
                        throw new DataStorageException("Failed to create ingredient recipe relationship: " + entity);
                    }
                }

                return findById(recipeId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public Collection<RecipeEntity> findAll() {
        var sql = """
                SELECT id,
                       name,
                       description,
                       instructions,
                       categoryID,
                       portions,
                       duration
                    FROM Recipe
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<RecipeEntity> recipes = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var recipe = recipeFromResultSet(resultSet);
                    recipes.add(recipe);
                }
            }

            return recipes;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all recipes", ex);
        }
    }

    @Override
    public Optional<RecipeEntity> findById(long id) {
        var sql = """
                SELECT id,
                       name,
                       description,
                       instructions,
                       categoryID,
                       portions,
                       duration
                    FROM Recipe
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipe by id: " + id, ex);
        }
    }

    private Map<Long, Integer> findAllIngredients(Long recipeId) {
        var sql = """
                SELECT ingredientID,
                       amount
                    FROM IngredientRecipe
                    WHERE recipeID = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, recipeId);
            Map<Long, Integer> ingredientAmounts = new HashMap<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ingredientAmounts.put(
                            resultSet.getLong("ingredientID"),
                            resultSet.getInt("amount")
                    );
                }
            }

            return ingredientAmounts;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all recipes", ex);
        }
    }

    @Override
    public RecipeEntity update(RecipeEntity entity) {
        Objects.requireNonNull(entity.id(), "Entity id cannot be null");

        final var sql = """
                UPDATE Recipe
                    SET
                    name = ?,
                    description = ?,
                    instructions = ?,
                    categoryID = ?,
                    portions = ?,
                    duration = ?
                    WHERE id = ?
                """;

        final var sqlIngredientsDelete = """
                DELETE FROM IngredientRecipe
                    WHERE recipeID = ?;
                """;

        final var sqlIngredientsCreate = """
                INSERT INTO  IngredientRecipe (ingredientID, recipeID, amount) VALUES (?, ?, ?);
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);

                var connectionIngredientsDelete = connections.get();
                var statementIngredientsDelete = connectionIngredientsDelete.use().prepareStatement(sqlIngredientsDelete);

                var connectionIngredientsCreate = connections.get();
                var statementIngredientsCreate = connectionIngredientsCreate.use().prepareStatement(sqlIngredientsCreate);
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.description());
            statement.setString(3, entity.instructions());
            statement.setLong(4, entity.categoryId());
            statement.setInt(5, entity.portions());
            statement.setInt(6, entity.duration());
            statement.setLong(7, entity.id());

            if (statement.executeUpdate() == 0) {
                throw new DataStorageException("Failed to update non-existing recipe: " + entity);
            }

            statementIngredientsDelete.setLong(1, entity.id());
            if (statementIngredientsDelete.executeUpdate() == 0) {
                throw new DataStorageException("Failed to update non-existing recipe: " + entity);
            }

            statementIngredientsCreate.setLong(2, entity.id());
            for (var entry : entity.ingredientsAmounts().entrySet()) {
                statementIngredientsCreate.setLong(1, entry.getKey());
                statementIngredientsCreate.setInt(3, entry.getValue());

                if (statementIngredientsCreate.executeUpdate() == 0) {
                    throw new DataStorageException("Failed to create ingredient recipe relationship: " + entity);
                }
            }

        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update recipe with id: " + entity.id(), ex);
        }

        return findById(entity.id()).orElseThrow();

    }

    @Override
    public void deleteById(long entityId) {
        var sql = "DELETE FROM Recipe WHERE id = ?";

        var sqlIngredients = "DELETE FROM IngredientRecipe WHERE recipeID = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);


                var connectionIngredients = connections.get();
                var statementIngredients = connectionIngredients.use().prepareStatement(sqlIngredients);
        ) {
            statementIngredients.setLong(1, entityId);
            int rowsUpdated = statementIngredients.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("recipe not found %d".formatted(entityId));
            }

            statement.setLong(1, entityId);
            rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("recipe not found %d".formatted(entityId));
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException(
                        "More then 1 recipe (rows=%d) has been deleted: %d".formatted(rowsUpdated, entityId));
            }


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(MainWindow.getFrame(), "Cannot delete recipe");
            throw new DataStorageException("Failed to delete recipe %d".formatted(entityId), ex);
        }
    }

    @Override
    public int deleteAll() {
        var sql = "DELETE FROM Recipe";
        var sqlIngredients = "DELETE FROM IngredientRecipe";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);
                var connectionIngredients = connections.get();
                var statementIngredients = connectionIngredients.use().prepareStatement(sqlIngredients);
        ){
            statementIngredients.executeUpdate();
            int result = statement.executeUpdate();
            return result;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all recipes", ex);
        }
    }

    private RecipeEntity recipeFromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("instructions"),
                findAllIngredients(resultSet.getLong("id")),
                resultSet.getLong("categoryID"),
                resultSet.getInt("portions"),
                resultSet.getInt("duration")
        );
    }
}
