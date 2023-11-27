package cz.muni.fi.pv168.project.persistance.dao;

import cz.muni.fi.pv168.project.persistance.db.ConnectionHandler;
import cz.muni.fi.pv168.project.persistance.entity.UnitEntity;
import cz.muni.fi.pv168.project.persistance.exception.DataStorageException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Supplier;

public class UnitDao implements DataAccessObject<UnitEntity> {
    private final Supplier<ConnectionHandler> connections;

    public UnitDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public UnitEntity create(UnitEntity entity) {
        String sql = "INSERT INTO  Unit (name, abbreviation, convertionFactor, baseUnitID) VALUES (?, ?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.abbreviation());
            statement.setLong(3, entity.conversionFactor());
            statement.setLong(4, entity.baseUnitID());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long unitId;

                if (keyResultSet.next()) {
                    unitId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(unitId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public Collection<UnitEntity> findAll() {
        var sql = """
                SELECT id,
                       name,
                       abbreviation,
                       convertionFactor,
                       baseUnitID
                    FROM Unit
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<UnitEntity> units = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var unit = unitFromResultSet(resultSet);
                    units.add(unit);
                }
            }

            return units;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all units", ex);
        }
    }

    @Override
    public Optional<UnitEntity> findById(long id) {
        var sql = """
                SELECT id,
                       name,
                       abbreviation,
                       convertionFactor,
                       baseUnitID
                    FROM Unit
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(unitFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load unit by id: " + id, ex);
        }
    }

    @Override
    public UnitEntity update(UnitEntity entity) {
        Objects.requireNonNull(entity.id(), "Entity id cannot be null");

        final var sql = """
                UPDATE Unit
                    SET
                    name = ?,
                    abbreviation = ?,
                    convertionFactor = ?,
                    baseUnitID = ?
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.abbreviation());
            statement.setLong(3, entity.conversionFactor());
            statement.setLong(4, entity.baseUnitID());
            statement.setLong(5, entity.id());

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
        var sql = "DELETE FROM Unit WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);
        ) {
            statement.setLong(1, entityId);
            var rowsUpdated = statement.executeUpdate();
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
        var sql = "DELETE FROM Unit";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ){
            return statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all units", ex);
        }
    }

    private UnitEntity unitFromResultSet(ResultSet resultSet) throws SQLException {
        return new UnitEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("abbreviation"),
                resultSet.getLong("convertionFactor"),
                resultSet.getLong("baseUnitID")
        );
    }
}
