package cz.muni.fi.pv168.project.persistance.dao;

import cz.muni.fi.pv168.project.persistance.db.ConnectionHandler;
import cz.muni.fi.pv168.project.persistance.entity.BaseUnitEntity;
import cz.muni.fi.pv168.project.persistance.exception.DataStorageException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Supplier;

public class BaseUnitDao implements DataAccessObject<BaseUnitEntity> {

    private final Supplier<ConnectionHandler> connections;

    public BaseUnitDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }


    @Override
    public BaseUnitEntity create(BaseUnitEntity entity) {
        String sql = "INSERT INTO  BaseUnit (name, abbreviation) VALUES (?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.abbreviation());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long baseUnitId;

                if (keyResultSet.next()) {
                    baseUnitId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(baseUnitId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public Collection<BaseUnitEntity> findAll() {
        var sql = """
                SELECT id,
                       name,
                       abbreviation
                    FROM BaseUnit
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<BaseUnitEntity> baseUnits = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var baseUnit = baseUnitFromResultSet(resultSet);
                    baseUnits.add(baseUnit);
                }
            }

            return baseUnits;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all units", ex);
        }
    }

    @Override
    public Optional<BaseUnitEntity> findById(long id) {
        var sql = """
                SELECT id,
                       name,
                       abbreviation
                    FROM BaseUnit
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(baseUnitFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load unit by id: " + id, ex);
        }
    }

    @Override
    public BaseUnitEntity update(BaseUnitEntity entity) {
        Objects.requireNonNull(entity.id(), "Entity id cannot be null");

        final var sql = """
                UPDATE BaseUnit
                    SET
                    name = ?,
                    abbreviation = ?
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.abbreviation());
            statement.setLong(3, entity.id());

            if (statement.executeUpdate() == 0) {
                throw new DataStorageException("Failed to update non-existing unit: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update unit with id: " + entity.id(), ex);
        }

        return findById(entity.id()).orElseThrow();
    }

    @Override
    public void deleteById(long entityId) {
        var sql = "DELETE FROM BaseUnit WHERE id = ?";
        var sqlUnits = "DELETE FROM Unit WHERE baseUnitID = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);

                var connectionUnit = connections.get();
                var statementUnit = connectionUnit.use().prepareStatement(sqlUnits);
        ) {
            statementUnit.setLong(1, entityId);
            int rowsUpdated = statementUnit.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Base unit not found %d".formatted(entityId));
            }

            statement.setLong(1, entityId);
            rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("unit not found %d".formatted(entityId));
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException(
                        "More then 1 unit (rows=%d) has been deleted: %d".formatted(rowsUpdated, entityId));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete unit %d".formatted(entityId), ex);
        }
    }

    @Override
    public int deleteAll() {
        var sql = "DELETE FROM BaseUnit";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ){
            return statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all base units", ex);
        }
    }

    private BaseUnitEntity baseUnitFromResultSet(ResultSet resultSet) throws SQLException {
        return new BaseUnitEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("abbreviation")
        );
    }


}
