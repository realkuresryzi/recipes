package cz.muni.fi.pv168.project.persistance.dao;

import cz.muni.fi.pv168.project.persistance.db.ConnectionHandler;
import cz.muni.fi.pv168.project.persistance.entity.CategoryEntity;
import cz.muni.fi.pv168.project.persistance.exception.DataStorageException;
import cz.muni.fi.pv168.project.ui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.*;
import java.util.function.Supplier;

public class CategoryDao implements DataAccessObject<CategoryEntity> {

    private final Supplier<ConnectionHandler> connections;

    public CategoryDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public CategoryEntity create(CategoryEntity entity) {
        String sql = "INSERT INTO  Category (name, description, color) VALUES (?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.description());
            statement.setInt(3, entity.color().getRGB());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long categoryId;

                if (keyResultSet.next()) {
                    categoryId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(categoryId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public Collection<CategoryEntity> findAll() {
        var sql = """
                SELECT id,
                       name,
                       description,
                       color
                    FROM Category
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<CategoryEntity> categories = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var category = categoryFromResultSet(resultSet);
                    categories.add(category);
                }
            }

            return categories;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all categories", ex);
        }
    }

    @Override
    public Optional<CategoryEntity> findById(long id) {
        var sql = """
                SELECT id,
                       name,
                       description,
                       color
                    FROM Category
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(categoryFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load category by id: " + id, ex);
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity entity) {
        Objects.requireNonNull(entity.id(), "Entity id cannot be null");

        final var sql = """
                UPDATE Category
                    SET
                    name = ?,
                    description = ?,
                    color = ?
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.description());
            statement.setLong(3, entity.color().getRGB());
            statement.setLong(4, entity.id());

            if (statement.executeUpdate() == 0) {
                throw new DataStorageException("Failed to update non-existing category: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update category with id: " + entity.id(), ex);
        }

        return findById(entity.id()).orElseThrow();
    }

    @Override
    public void deleteById(long entityId) {
        var sql = "DELETE FROM Category WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entityId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("category not found %d".formatted(entityId));
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException(
                        "More then 1 category (rows=%d) has been deleted: %d".formatted(rowsUpdated, entityId));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(MainWindow.getFrame(), "You need to remove recipes from category first");
            throw new DataStorageException("Failed to delete category %d".formatted(entityId), ex);
        }
    }

    @Override
    public int deleteAll() {
        var sql = "DELETE FROM Category";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ){
            return statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all categories", ex);
        }
    }

    private CategoryEntity categoryFromResultSet(ResultSet resultSet) throws SQLException {
        return new CategoryEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                new Color(resultSet.getInt("color"))
        );
    }
}
