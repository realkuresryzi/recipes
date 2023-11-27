package cz.muni.fi.pv168.project.utils.exporters.formats;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.utils.DataFileObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class XMLExporter implements FormattedExporter {
    private static void writeXml(Document doc, OutputStream output) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }

    public Element exportUnits(Document doc, List<Unit> unitList) {
        Element units = doc.createElement("units");
        for (Unit unit : unitList) {
            Element name = doc.createElement("name");
            name.setTextContent(unit.getName());
            Element abbreviation = doc.createElement("abbreviation");
            abbreviation.setTextContent(unit.getAbbreviation());
            Element conversionFactor = doc.createElement("factor");
            conversionFactor.setTextContent(((Long) unit.getConversionFactor()).toString());
            Element baseUnit = doc.createElement("base");
            Integer baseUnitIndex = -1;
            for (int j = 0; j < unitList.size(); j++) {
                Unit unitToCheck = unitList.get(j);
                if (unitToCheck.getName().equals(unit.getBaseUnit().getName())) {
                    baseUnitIndex = j;
                }
            }
            baseUnit.setTextContent(baseUnitIndex.toString());

            Element unitElement = doc.createElement("unit");
            unitElement.appendChild(name);
            unitElement.appendChild(abbreviation);
            unitElement.appendChild(conversionFactor);
            unitElement.appendChild(baseUnit);
            units.appendChild(unitElement);
        }
        return units;
    }

    public Element exportCategories(Document doc, List<Category> categoryList) {
        Element categories = doc.createElement("categories");
        for (Category category : categoryList) {
            Element name = doc.createElement("name");
            name.setTextContent(category.getName());
            Element description = doc.createElement("description");
            description.setTextContent(category.getDescription());
            Element color = doc.createElement("color");
            Element red = doc.createElement("red");
            red.setTextContent(((Integer) category.getColor().getRed()).toString());
            Element green = doc.createElement("green");
            green.setTextContent(((Integer) category.getColor().getGreen()).toString());
            Element blue = doc.createElement("blue");
            blue.setTextContent(((Integer) category.getColor().getBlue()).toString());
            color.appendChild(red);
            color.appendChild(green);
            color.appendChild(blue);

            Element categoryElement = doc.createElement("category");
            categoryElement.appendChild(name);
            categoryElement.appendChild(description);
            categoryElement.appendChild(color);
            categories.appendChild(categoryElement);
        }
        return categories;
    }

    public Element exportIngredients(Document doc, List<Ingredient> ingredientList, List<Unit> unitList) {
        Element ingredients = doc.createElement("ingredients");
        for (Ingredient ingredient : ingredientList) {
            Element name = doc.createElement("name");
            name.setTextContent(ingredient.getName());
            Element nutritionalValue = doc.createElement("nutritionalValue");
            nutritionalValue.setTextContent(((Integer) ingredient.getNutritionValue()).toString());
            Element unit = doc.createElement("unit");
            unit.setTextContent(((Integer) unitList.indexOf(ingredient.getUnit())).toString());

            Element ingredientElement = doc.createElement("ingredient");
            ingredientElement.appendChild(name);
            ingredientElement.appendChild(nutritionalValue);
            ingredientElement.appendChild(unit);
            ingredients.appendChild(ingredientElement);
        }
        return ingredients;
    }

    public Element exportRecipes(Document doc, List<Recipe> recipeList, List<Category> categoryList, List<Ingredient> ingredientList) {
        Element recipes = doc.createElement("recipes");
        for (Recipe recipe : recipeList) {
            Element name = doc.createElement("name");
            name.setTextContent(recipe.getName());
            Element description = doc.createElement("description");
            description.setTextContent(recipe.getDescription());
            Element instructions = doc.createElement("instructions");
            instructions.setTextContent(recipe.getInstructions());
            Element portions = doc.createElement("portions");
            portions.setTextContent(((Integer) recipe.getPortions()).toString());
            Element duration = doc.createElement("duration");
            duration.setTextContent(((Integer) recipe.getDuration()).toString());
            Element category = doc.createElement("category");
            category.setTextContent(((Integer) categoryList.indexOf(recipe.getCategory())).toString());
            Element ingredients = doc.createElement("ingredients");

            for (Map.Entry<Ingredient, Integer> ingredient : recipe.getIngredients().entrySet()) {
                Element ingredientElem = doc.createElement("ingredient");
                Element nameElem = doc.createElement("name");
                Element amountElem = doc.createElement("amount");
                nameElem.setTextContent(ingredient.getKey().getName());
                amountElem.setTextContent(ingredient.getValue().toString());
                ingredientElem.appendChild(nameElem);
                ingredientElem.appendChild(amountElem);
                ingredients.appendChild(ingredientElem);
            }

            Element recipeElem = doc.createElement("recipe");
            recipeElem.appendChild(name);
            recipeElem.appendChild(description);
            recipeElem.appendChild(instructions);
            recipeElem.appendChild(portions);
            recipeElem.appendChild(duration);
            recipeElem.appendChild(category);
            recipeElem.appendChild(ingredients);
            recipes.appendChild(recipeElem);
        }
        return recipes;
    }

    public void exportFile(String exportFilePath, DataFileObject data) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("recipesDB");
            doc.appendChild(root);

            root.appendChild(exportUnits(doc, data.unitList));
            root.appendChild(exportCategories(doc, data.categoryList));
            root.appendChild(exportIngredients(doc, data.ingredientList, data.unitList));
            root.appendChild(exportRecipes(doc, data.recipeList, data.categoryList, data.ingredientList));

            writeXml(doc, Files.newOutputStream(Paths.get(exportFilePath)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
