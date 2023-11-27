package cz.muni.fi.pv168.project.persistance.entity;

import java.awt.*;

/**
 * Represents the Category entity
 *
 * @param id          Primary key in the database (sequential)
 * @param name        Name of the category
 * @param description Description of the category
 * @param color       Color of the category
 */
public record CategoryEntity(
        Long id,
        String name,
        String description,
        Color color
) {
    public CategoryEntity(
            String name,
            String description,
            Color color
    ) {
        this(null, name, description, color);
    }
}
