package cz.muni.fi.pv168.project.persistance.entity;

/**
 * Represents the BaseUnit entity
 *
 * @param id           Primary key in the database (sequential)
 * @param name         Name of the unit
 * @param abbreviation Abbreviation of the unit
 */
public record BaseUnitEntity(
        Long id,
        String name,
        String abbreviation
) {
    public BaseUnitEntity(
            String name,
            String abbreviation
    ) {
        this(null, name, abbreviation);
    }
}
