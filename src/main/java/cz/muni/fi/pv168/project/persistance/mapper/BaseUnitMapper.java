package cz.muni.fi.pv168.project.persistance.mapper;

import cz.muni.fi.pv168.project.model.BaseUnit;
import cz.muni.fi.pv168.project.persistance.entity.BaseUnitEntity;
import cz.muni.fi.pv168.project.persistance.validation.Validator;

public class BaseUnitMapper implements EntityMapper<BaseUnitEntity, BaseUnit> {

    private final Validator<BaseUnit> baseUnitValidator;

    public BaseUnitMapper(Validator<BaseUnit> validator) {
        this.baseUnitValidator = validator;
    }

    @Override
    public BaseUnit mapToModel(BaseUnitEntity entity) {
        return new BaseUnit(
                entity.id(),
                entity.name(),
                entity.abbreviation()
        );
    }

    @Override
    public BaseUnitEntity mapToEntity(BaseUnit source) {
        baseUnitValidator.validate(source).intoException();

        return new BaseUnitEntity(
                source.getId(),
                source.getName(),
                source.getAbbreviation()
        );
    }

}
