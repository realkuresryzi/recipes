package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.BaseUnit;
import cz.muni.fi.pv168.project.persistance.repository.Repository;

import java.util.List;

public class BaseUnitRepositoryWrapper implements RepositoryWrapper<BaseUnit> {

    private final Repository<BaseUnit> baseUnits;

    public BaseUnitRepositoryWrapper(Repository<BaseUnit> baseUnits) {
        this.baseUnits = baseUnits;
    }

    public void addRow(BaseUnit baseUnit) {
        baseUnits.create(baseUnit);
    }

    public void updateRow(BaseUnit baseUnit) {
        baseUnits.update(baseUnit);
    }

    public void deleteRow(int rowIndex) {
        baseUnits.deleteByIndex(rowIndex);
    }

    public BaseUnit getEntity(int rowIndex) {
        return baseUnits.findByIndex(rowIndex).orElseThrow();
    }

    public List<BaseUnit> getEntities() {
        return baseUnits.findAll();
    }

    public BaseUnit getEntityByName(String name) {
        for (BaseUnit unit : baseUnits.findAll()) {
            if (unit.getName().equals(name)) {
                return unit;
            }
        }
        return null;
    }

    public void refresh() {
        baseUnits.refresh();
    }
}
