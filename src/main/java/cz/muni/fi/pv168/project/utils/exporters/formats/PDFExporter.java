package cz.muni.fi.pv168.project.utils.exporters.formats;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.utils.DataFileObject;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PDFExporter implements FormattedExporter {

    Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
    Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);

    private void exportUnits(Document document, List<Unit> units) {
        String unitText = "Units:\n\n";
        List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph(unitText, sectionFont));

        for (Unit unit : units) {
            unitText = unit.getName() + " (" + unit.getAbbreviation() + ")" + "\n";
            paragraphs.add(new Paragraph(unitText, headerFont));
            unitText = "Conversion form base unit: 1 " + unit.getAbbreviation() + " = "
                    + unit.getConversionFactor() + " " + unit.getBaseUnit().getAbbreviation() + "\n\n";
            paragraphs.add(new Paragraph(unitText, textFont));
        }

        try {
            for (Paragraph p : paragraphs) {
                document.add(p);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void exportCategories(Document document, List<Category> categories) {
        List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph("Categories:\n\n", sectionFont));

        for (Category category : categories) {
            Font categoryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14,
                    new BaseColor(category.getColor().getRed(), category.getColor().getGreen(),
                            category.getColor().getBlue()));
            paragraphs.add(new Paragraph(category.getName(), categoryFont));
            paragraphs.add(new Paragraph(category.getDescription(), textFont));
        }

        try {
            for (Paragraph p : paragraphs) {
                document.add(p);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void exportIngredients(Document document, List<Ingredient> ingredients, List<Unit> units) {
        String ingredientText = "\nIngredients: \n\n";
        List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph(ingredientText, sectionFont));

        for (Ingredient ingredient : ingredients) {
            ingredientText = "";
            paragraphs.add(new Paragraph(ingredient.getName(), headerFont));
            ingredientText += "Nutritional value: " + ingredient.getNutritionValue() + " kcal\n\n";
            paragraphs.add(new Paragraph(ingredientText, textFont));
        }

        try {
            for (Paragraph p : paragraphs) {
                document.add(p);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void exportRecipes(Document document, List<Recipe> recipes, List<Category> categories, List<Ingredient> ingredients) {
        String recipeText = "Recipes: \n\n";
        List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph(recipeText, sectionFont));

        for (Recipe recipe : recipes) {
            recipeText = "";
            paragraphs.add(new Paragraph(recipe.getName(), headerFont));
            recipeText += "Description: " + recipe.getDescription() + "\nInstructions: ";
            if (recipe.getIngredients().isEmpty()) {
                recipeText += "\nNo ingredients listed!";
            } else {
                recipeText += recipe.getInstructions() + "\nIngredients:";
                for (Map.Entry<Ingredient, Integer> ingredient : recipe.getIngredients().entrySet()) {
                    recipeText += "\n    " + ingredient.getKey() + " - " + ingredient.getValue() + " " + ingredient.getKey().getUnit().getAbbreviation();
                }
            }
            recipeText += "\nPortions: " + recipe.getPortions() + "\nDuration: ";
            recipeText += recipe.getDuration() + " min\nCategory: ";
            recipeText += recipe.getCategory().getName() + "\n\n";
            paragraphs.add(new Paragraph(recipeText, textFont));
        }
        try {
            for (Paragraph p : paragraphs) {
                document.add(p);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void exportFile(String filePath, DataFileObject data) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();

            exportRecipes(document, data.recipeList, data.categoryList, data.ingredientList);
            document.newPage();
            exportCategories(document, data.categoryList);
            document.newPage();
            exportIngredients(document, data.ingredientList, data.unitList);
            document.newPage();
            exportUnits(document, data.unitList);

            document.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
