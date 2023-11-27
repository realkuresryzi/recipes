package cz.muni.fi.pv168.project.model;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Category {

    private static Long ID = new Long(0);
    private Long id;

    private String name;

    private String description;
    private Color color;

    public Category(Long id, String name, String description, Color color) {
        ID = id;
        setId(id);
        setName(name);
        setDescription(description);
        setColor(color);
    }

    public Category(String name, String description, Color color) {
        id = ++ID;
        setName(name);
        setDescription(description);
        setColor(color);
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public static List<Category> getTestCategories() {
        List<Category> categories = List.of(
                new Category("None", "No category selected", Color.WHITE)
                , new Category("Cake", "Wonderful cakes", Color.RED)
                , new Category("Soup", "Soups for your soul", Color.BLUE)
                , new Category("Drink", "Amazing drinks", Color.YELLOW)
                , new Category("Meat", "For carnivores", Color.GREEN)
                , new Category("Breakfast", "Morning birds jump further", Color.MAGENTA)
                , new Category("Toast", "TOASTS!", Color.ORANGE)
        );
        return categories;
    }


    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name)
                && description.equals(category.description)
                && color.equals(category.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, color);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
