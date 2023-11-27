package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.persistance.repository.Repository;

import java.util.List;

public class UnitRepositoryWrapper implements RepositoryWrapper<Unit> {

    private final Repository<Unit> units;

    public UnitRepositoryWrapper(Repository<Unit> units) {
        this.units = units;
    }

    public void addRow(Unit unit) {
        units.create(unit);
    }

    public void updateRow(Unit unit) {
        units.update(unit);
    }

    public void deleteRow(int rowIndex) {
        units.deleteByIndex(rowIndex);
    }

    public Unit getEntity(int rowIndex) {
        return units.findByIndex(rowIndex).orElseThrow();
    }

    public List<Unit> getEntities() {
        return units.findAll();
    }

    public Unit getEntityByName(String name) {
        for (Unit unit : units.findAll()) {
            if (unit.getName().equals(name)) {
                return unit;
            }
        }
        return null;
    }

    public void refresh() {
        units.refresh();
    }


}
