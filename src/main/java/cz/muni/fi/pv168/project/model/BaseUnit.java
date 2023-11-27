package cz.muni.fi.pv168.project.model;

import java.util.Objects;

public class BaseUnit {
    private static Long ID = new Long(0);
    private Long id;
    private String name;
    private String abbreviation;

    public BaseUnit(String name, String abbreviation) {
        this.id = ++ID;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public BaseUnit(Long id, String name, String abbreviation) {
        ID = id;
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseUnit baseUnit = (BaseUnit) o;
        return Objects.equals(id, baseUnit.id) && Objects.equals(name, baseUnit.name) && Objects.equals(abbreviation, baseUnit.abbreviation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, abbreviation);
    }
}
