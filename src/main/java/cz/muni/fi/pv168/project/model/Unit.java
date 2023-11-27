package cz.muni.fi.pv168.project.model;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

public class Unit {
    private Long ID = new Long(0);
    private Long id;
    private String name;
    private String abbreviation;
    private long conversionFactor;
    private BaseUnit baseUnit;

    public Unit(String name, String abbreviation, long conversionFactor, BaseUnit baseUnit) {
        this.id = ++ID;
        this.name = name;
        this.abbreviation = abbreviation;
        this.conversionFactor = conversionFactor;
        this.baseUnit = baseUnit;
    }

    public Unit(Long id, String name, String abbreviation, long conversionFactor, BaseUnit baseUnit) {
        ID = id;
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.conversionFactor = conversionFactor;
        this.baseUnit = baseUnit;
    }

    public static List<Unit> getTestUnits() {
        List<Unit> units = List.of(
                new Unit("Gram", "g", 1, null),
                new Unit("Kilogram", "kg", 1000, null),
                new Unit("Milliliter", "ml", 1, null),
                new Unit("Liter", "l", 1000, null),
                new Unit("Teaspoon", "tsp", 5, null),
                new Unit("Tablespoon", "tbsp", 15, null),
                new Unit("Cup", "cup", 240, null),
                new Unit("Piece", "pc", 1, null)
        );
        for (Unit unit : units) {
            unit.setBaseUnit(new BaseUnit("gram", "g"));
        }
        return units;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public long getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(Integer conversionFactor) {
        this.conversionFactor = conversionFactor.longValue();
    }

    public BaseUnit getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(BaseUnit baseUnit) {
        this.baseUnit = baseUnit;
    }
    //TODO: Add base Units to test units.

    @Override
    public String toString() {
        return MessageFormat.format("{0}, ({1})", name, abbreviation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return conversionFactor == unit.conversionFactor
                && name.equals(unit.name)
                && abbreviation.equals(unit.abbreviation)
                && baseUnit.getName().equals(unit.baseUnit.getName())
                && baseUnit.getAbbreviation().equals(unit.baseUnit.getAbbreviation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, abbreviation, conversionFactor);
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}