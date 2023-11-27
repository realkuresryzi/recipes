package cz.muni.fi.pv168.project.persistance.entity;

/**
 * Represents the Unit entity
 *
 * @param id               Primary key in the database (sequential)
 * @param name             Name of the unit
 * @param abbreviation     Abbreviation of the unit
 * @param conversionFactor Conversion factor from base unit
 * @param baseUnitID       Base unit ID for the conversion
 */
public record UnitEntity(
        Long id,
        String name,
        String abbreviation,
        Long conversionFactor,
        Long baseUnitID
) {
    public UnitEntity(
            String name,
            String abbreviation,
            Long conversionFactor,
            Long baseUnitID
    ) {
        this(null, name, abbreviation, conversionFactor, baseUnitID);
    }
}
