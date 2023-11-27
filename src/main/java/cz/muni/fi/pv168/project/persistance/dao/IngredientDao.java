package cz.muni.fi.pv168.project.persistance.dao;

import cz.muni.fi.pv168.project.persistance.db.ConnectionHandler;
import cz.muni.fi.pv168.project.persistance.entity.IngredientEntity;
import cz.muni.fi.pv168.project.persistance.exception.DataStorageException;
import cz.muni.fi.pv168.project.ui.MainWindow;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Supplier;

public class IngredientDao implements DataAccessObject<IngredientEntity> {
    private final Supplier<ConnectionHandler> connections;

    public IngredientDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public IngredientEntity create(IngredientEntity entity) {
        String sql = "INSERT INTO  Ingredient (name, nutritionalValue, unitID) VALUES (?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setInt(2, entity.nutritionValue());
            statement.setLong(3, entity.unitId());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long ingredientId;

                if (keyResultSet.next()) {
                    ingredientId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(ingredientId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public Collection<IngredientEntity> findAll() {
        var sql = """
                SELECT id,
                       name,
                       nutritionalValue,
                       unitID
                    FROM Ingredient
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<IngredientEntity> ingredients = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var ingredient = ingredientFromResultSet(resultSet);
                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all ingredients", ex);
        }
    }

    @Override
    public Optional<IngredientEntity> findById(long id) {
        var sql = """
                SELECT id,
                       name,
                       nutritionalValue,
                       unitID
                    FROM Ingredient
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient by id: " + id, ex);
        }
    }

    @Override
    public IngredientEntity update(IngredientEntity entity) {
        Objects.requireNonNull(entity.id(), "Entity id cannot be null");

        final var sql = """
                UPDATE Ingredient
                    SET
                    name = ?,
                    nutritionalValue = ?,
                    unitID = ?
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, entity.name());
            statement.setInt(2, entity.nutritionValue());
            statement.setLong(3, entity.unitId());
            statement.setLong(4, entity.id());

            if (statement.executeUpdate() == 0) {
                throw new DataStorageException("Failed to update non-existing ingredient: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update ingredient with id: " + entity.id(), ex);
        }

        return findById(entity.id()).orElseThrow();
    }

    @Override
    public void deleteById(long entityId) {
        var sql = "DELETE FROM Ingredient WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entityId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("ingredient not found %d".formatted(entityId));
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException(
                        "More then 1 ingredient (rows=%d) has been deleted: %d".formatted(rowsUpdated, entityId));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(MainWindow.getFrame(), "You need to remove ingredient from recipes first");
            throw new DataStorageException("Failed to delete ingredient %d".formatted(entityId), ex);
        }
    }

    @Override
    public int deleteAll() {
        var sql = "DELETE FROM Ingredient";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ){
            return statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all ingredients", ex);
        }
    }

    private IngredientEntity ingredientFromResultSet(ResultSet resultSet) throws SQLException {
        return new IngredientEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("unitID"),
                resultSet.getInt("nutritionalValue")
        );
    }
}
