package cz.muni.fi.pv168.project.utils.importers.formats;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.utils.DataFileObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class encapsulating all functions for XML import and export.
 */
public class XMLImporter implements FormattedImporter {

    private List<Unit> parseUnits(Document doc, List<BaseUnit> baseUnits) {
        List<Unit> unitList = new ArrayList<>();
        Node unitsNode = doc.getElementsByTagName("units").item(0);
        if (unitsNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new RuntimeException("Wrong import file.");
        }
        Element unitsElement = (Element) unitsNode;
        NodeList nodeList = unitsElement.getElementsByTagName("unit");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                unitList.add(new Unit(
                        element.getElementsByTagName("name").item(0).getTextContent(),
                        element.getElementsByTagName("abbreviation").item(0).getTextContent(),
                        Long.parseLong(element.getElementsByTagName("factor").item(0).getTextContent()),
                        baseUnits.get(Integer.parseInt(element.getElementsByTagName("base").item(0).getTextContent()))
                ));
            }
        }

        return unitList;
    }

    private List<Category> parseCategories(Document doc) {
        List<Category> categoryList = new ArrayList<>();

        Node unitsNode = doc.getElementsByTagName("categories").item(0);
        if (unitsNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new RuntimeException("Wrong import file.");
        }
        Element unitsElement = (Element) unitsNode;
        NodeList nodeList = unitsElement.getElementsByTagName("category");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Element colorNode = (Element) element.getElementsByTagName("color").item(0);

                Color categoryColor = new Color(
                        Integer.parseInt(colorNode.getElementsByTagName("red").item(0).getTextContent()),
                        Integer.parseInt(colorNode.getElementsByTagName("green").item(0).getTextContent()),
                        Integer.parseInt(colorNode.getElementsByTagName("blue").item(0).getTextContent())
                );
                categoryList.add(new Category(
                        1L,
                        element.getElementsByTagName("name").item(0).getTextContent(),
                        element.getElementsByTagName("description").item(0).getTextContent(),
                        categoryColor
                ));
            }
        }

        return categoryList;
    }

    private List<Ingredient> parseIngredients(Document doc, List<Unit> unitList) {
        List<Ingredient> ingredientList = new ArrayList<>();

        Node unitsNode = doc.getElementsByTagName("ingredients").item(0);
        if (unitsNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new RuntimeException("Wrong import file.");
        }
        Element unitsElement = (Element) unitsNode;
        NodeList nodeList = unitsElement.getElementsByTagName("ingredient");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                ingredientList.add(new Ingredient(
                        element.getElementsByTagName("name").item(0).getTextContent(),
                        unitList.get(Integer.parseInt(element.getElementsByTagName("unit").item(0).getTextContent())),
                        Integer.parseInt(element.getElementsByTagName("nutritionalValue").item(0).getTextContent())
                ));
            }
        }

        return ingredientList;
    }


    private List<Recipe> parseRecipes(Document doc, List<Ingredient> ingredientList, List<Category> categoryList) {
        List<Recipe> recipeList = new ArrayList<>();

        Node unitsNode = doc.getElementsByTagName("recipes").item(0);
        if (unitsNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new RuntimeException("Wrong import file.");
        }
        Element unitsElement = (Element) unitsNode;
        NodeList nodeList = unitsElement.getElementsByTagName("recipe");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Map<Ingredient, Integer> ingredients = new HashMap<>();
                Element ingredientsElement = (Element) element.getElementsByTagName("ingredients").item(0);
                NodeList ingredientNodeList = ingredientsElement.getElementsByTagName("ingredient");
                for (int j = 0; j < ingredientNodeList.getLength(); j++) {
                    Element ingredient = (Element) ingredientNodeList.item(j);
                    String ingredientName = ingredient.getElementsByTagName("name").item(0).getTextContent();
                    ingredients.put(ingredientList.stream().filter(ing -> ingredientName.equals(ing.getName())).findFirst().get(),
                            Integer.parseInt(ingredient.getElementsByTagName("amount").item(0).getTextContent()));
                }

                recipeList.add(new Recipe(
                        element.getElementsByTagName("name").item(0).getTextContent(),
                        element.getElementsByTagName("description").item(0).getTextContent(),
                        (Map<Ingredient, Integer>) ingredients,
                        element.getElementsByTagName("instructions").item(0).getTextContent(),
                        categoryList.get(Integer.parseInt(element.getElementsByTagName("category").item(0).getTextContent())),
                        Integer.parseInt(element.getElementsByTagName("portions").item(0).getTextContent()),
                        Integer.parseInt(element.getElementsByTagName("duration").item(0).getTextContent())
                ));
            }
        }

        return recipeList;
    }

    public DataFileObject importFile(String filePath, List<BaseUnit> baseUnits) {
        File importFile = new File(filePath);
        DocumentBuilder documentBuilder;
        DataFileObject data = new DataFileObject();
        try {
            documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
            Document doc = documentBuilder.parse(importFile);
            data.unitList = parseUnits(doc, baseUnits);
            data.categoryList = parseCategories(doc);
            data.ingredientList = parseIngredients(doc, data.unitList);
            data.recipeList = parseRecipes(doc, data.ingredientList, data.categoryList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }


}
