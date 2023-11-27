package cz.muni.fi.pv168.project.persistance.mapper;

import cz.muni.fi.pv168.project.model.BaseUnit;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.persistance.entity.UnitEntity;
import cz.muni.fi.pv168.project.persistance.repository.Repository;
import cz.muni.fi.pv168.project.persistance.validation.Validator;

public class UnitMapper implements EntityMapper<UnitEntity, Unit> {

    private final Validator<Unit> unitValidator;

    private final Lookup<BaseUnit> baseUnitLookup;
    public UnitMapper(Repository<BaseUnit> baseUnitRepository, Validator<Unit> validator) {
        this.baseUnitLookup = baseUnitRepository::findById;
        this.unitValidator = validator;
    }

    public UnitMapper(Validator<Unit> unitValidator, Lookup<BaseUnit> baseUnitLookup) {
        this.unitValidator = unitValidator;
        this.baseUnitLookup = baseUnitLookup;
    }

    @Override
    public Unit mapToModel(UnitEntity entity) {
        BaseUnit baseUnit = baseUnitLookup.get(entity.baseUnitID()).orElseThrow();
        return new Unit(
                entity.id(),
                entity.name(),
                entity.abbreviation(),
                entity.conversionFactor(),
                baseUnit
        );
    }

    @Override
    public UnitEntity mapToEntity(Unit source) {
        unitValidator.validate(source).intoException();

        return new UnitEntity(
                source.getId(),
                source.getName(),
                source.getAbbreviation(),
                source.getConversionFactor(),
                source.getBaseUnit().getId()
        );
    }
}
