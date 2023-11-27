package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.persistance.validation.RecipeValidator;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class RecipeDialog extends EntityDialog<Recipe> {


    protected final JTextField nameField = new JTextField();
    protected final JTextField descriptionField = new JTextField();
    protected final JTextArea instructionsField = new JTextArea();
    protected final JTextArea ingredientsField = new JTextArea();
    protected final JSpinner amountField = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
    protected final ComboBoxModel<String> categoryComboBoxModel;
    protected final ComboBoxModel<String> ingredientsComboBoxModel;

    protected final JSpinner portionsField = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    protected final JSpinner durationField = new JSpinner(new SpinnerNumberModel(0, 0, 600, 5));

    protected final CategoryTableModel categoryTableModel;
    protected final IngredientTableModel ingredientTableModel;

    protected final JComboBox<String> categoryComboBox;
    protected final JComboBox<String> ingredientsComboBox;

    protected final List<Category> categoryList;
    protected final List<Ingredient> ingredientList;

    protected final JButton addIngredientButton = new JButton();
    protected final JButton resetIngredientButton = new JButton();

    protected Recipe recipe = new Recipe();

    protected Map<Ingredient, Integer> ingredients = new java.util.HashMap<>();

    protected String originalName;
    protected String originalDescription;
    protected String originalInstructions;
    protected Map<Ingredient, Integer> originalIngredients;
    protected int originalPortions;
    protected int originalDuration;



    public RecipeDialog(CategoryTableModel categoryTableModel, IngredientTableModel ingredientTableModel) {
        super(new RecipeValidator());

        this.categoryTableModel = categoryTableModel;
        this.ingredientTableModel = ingredientTableModel;

        categoryList = categoryTableModel.getEntities();
        ingredientList = ingredientTableModel.getEntities();

        categoryComboBoxModel = new DefaultComboBoxModel<>(categoryList.stream().map(Category::getName).toArray(String[]::new));
        ingredientsComboBoxModel = new DefaultComboBoxModel<>(ingredientList.stream().map(Ingredient::getName).toArray(String[]::new));

        categoryComboBox = new JComboBox<>(categoryComboBoxModel);
        ingredientsComboBox = new JComboBox<>(ingredientsComboBoxModel);

        addIngredientButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ingredientsField.setText(ingredientsField.getText() + ingredientsComboBox.getSelectedItem() + " "
                        + amountField.getValue() + " | ");
                ingredients.put(ingredientList.get(ingredientsComboBox.getSelectedIndex()), (Integer) amountField.getValue());

            }
        });
        addIngredientButton.setText("Add ingredient");

        resetIngredientButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ingredientsField.setText("");
                ingredients.clear();
            }
        });
        resetIngredientButton.setText("Reset all");

        setOriginalValues();

        this.setupRecipeDialog();
    }

    public RecipeDialog(Recipe recipe, CategoryTableModel categoryTableModel, IngredientTableModel ingredientTableModel) {
        this(categoryTableModel, ingredientTableModel);
        this.recipe = recipe;
        setValues();
    }

    private void setupRecipeDialog() {
        this.panel.setPreferredSize(new Dimension(450, 500));
        instructionsField.setMinimumSize(new Dimension(200, 100));
        instructionsField.setPreferredSize(new Dimension(200, 50));
        instructionsField.setLineWrap(true);
        ingredientsField.setMinimumSize(new Dimension(200, 100));
        ingredientsField.setPreferredSize(new Dimension(200, 50));
        ingredientsField.setLineWrap(true);
        ingredientsField.setEditable(false);
        addFields();
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        descriptionField.setText(recipe.getDescription());
        instructionsField.setText(recipe.getInstructions());
        portionsField.setValue(recipe.getPortions());
        durationField.setValue(recipe.getDuration());
        amountField.setValue(0);
        categoryComboBox.setSelectedItem(recipe.getCategory().getName());
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredients().entrySet()) {
            ingredientsField.append(entry.getKey().getName() + " " + entry.getValue() + " "
                    + entry.getKey().getUnit().getAbbreviation() + " | ");
            ingredients = recipe.getIngredients();
        }

        setOriginalValues();
    }

    private void addFields() {
        add("Name:", nameField);
        add("Description:", descriptionField);
        add("Instructions:", instructionsField);
        addWithConstraints("Category:", categoryComboBox, "gapleft 200, grow, align right");
        addWithConstraints("Portions:", portionsField, "gapleft 200, grow, align right");
        addWithConstraints("Duration:", durationField, "gapleft 200, grow, align right");
        add("", new JLabel(" "));
        var ingredientsLabel = new JLabel("Ingredients Manipulation");
        Font font = new Font("Courier", Font.BOLD, 15);
        ingredientsLabel.setFont(font);
        ingredientsLabel.setForeground(Color.BLUE);
        add("", ingredientsLabel);
        add("", new JLabel(" "));
        addWithConstraints("Ingredients:", ingredientsComboBox, "gapleft 200, grow, align right");
        addWithConstraints("Amount:", amountField, "gapleft 200, grow, align right");
        add(resetIngredientButton, addIngredientButton);
        add("Ingredients:", ingredientsField);
    }

    private void setOriginalValues() {
        originalName = recipe.getName();
        originalDescription = recipe.getDescription();
        originalInstructions = recipe.getInstructions();
        originalIngredients = recipe.getIngredients();
        originalPortions = recipe.getPortions();
        originalDuration = recipe.getDuration();
    }

    protected void hideButtons() {
        addIngredientButton.setVisible(false);
        resetIngredientButton.setVisible(false);
    }

    @Override
    void resetEntity() {
        recipe.setName(originalName);
        recipe.setDescription(originalDescription);
        recipe.setInstructions(originalInstructions);
        recipe.setIngredients(originalIngredients);
        recipe.setPortions(originalPortions);
        recipe.setDuration(originalDuration);
    }

    @Override
    Recipe getEntity() {
        recipe.setName(nameField.getText());
        recipe.setDescription(descriptionField.getText());
        recipe.setInstructions(instructionsField.getText());
        recipe.setPortions((Integer) portionsField.getValue());
        recipe.setDuration((Integer) durationField.getValue());
        recipe.setCategory(categoryTableModel.getEntityByName((String) categoryComboBoxModel.getSelectedItem()));
        recipe.setIngredients(ingredients);

        return recipe;
    }
}

