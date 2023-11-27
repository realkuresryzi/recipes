package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;

public class RecipeDetailDialog extends RecipeDialog {

    private Recipe recipe;

    public RecipeDetailDialog(Recipe recipe, CategoryTableModel categoryTableModel, IngredientTableModel ingredientTableModel) {
        super(recipe, categoryTableModel, ingredientTableModel);
        this.recipe = recipe;
        setValues();

        this.categoryComboBox.setEnabled(false);
        this.ingredientsComboBox.setEnabled(false);

        this.hideButtons();

    }

    private void setValues() {
        nameField.setText(recipe.getName());
        descriptionField.setText(recipe.getDescription());
        instructionsField.setText(recipe.getInstructions());
        portionsField.setValue(recipe.getPortions());
        durationField.setValue(recipe.getDuration());

        instructionsField.setEditable(false);
        nameField.setEditable(false);
        descriptionField.setEditable(false);
        portionsField.setEnabled(false);
        durationField.setEnabled(false);

        /*
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredients().entrySet()) {
            ingredientsField.append(entry.getKey().getName() + " " + entry.getValue() + " "
                    + entry.getKey().getUnit().getAbbreviation() + " | ");
        }

         */

    }


}
