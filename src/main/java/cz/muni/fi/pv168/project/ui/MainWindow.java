package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.model.BaseUnit;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.persistance.db.DatabaseManager;
import cz.muni.fi.pv168.project.ui.actions.*;
import cz.muni.fi.pv168.project.ui.components.*;
import cz.muni.fi.pv168.project.ui.dialog.*;
import cz.muni.fi.pv168.project.ui.enums.TabType;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.utils.DataFileObject;
import cz.muni.fi.pv168.project.utils.exporters.DaoExporter;
import cz.muni.fi.pv168.project.utils.exporters.Exporter;
import cz.muni.fi.pv168.project.utils.exporters.formats.ExporterFormatType;
import cz.muni.fi.pv168.project.utils.exporters.formats.JSONExporter;
import cz.muni.fi.pv168.project.utils.exporters.formats.PDFExporter;
import cz.muni.fi.pv168.project.utils.exporters.formats.XMLExporter;
import cz.muni.fi.pv168.project.utils.importers.Importer;
import cz.muni.fi.pv168.project.utils.importers.formats.ImporterFormatType;
import cz.muni.fi.pv168.project.utils.importers.formats.JSONImporter;
import cz.muni.fi.pv168.project.utils.importers.formats.XMLImporter;
import cz.muni.fi.pv168.project.utils.workers.AsyncExporter;
import cz.muni.fi.pv168.project.utils.workers.AsyncImporter;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class MainWindow {

    private static final JFrame frame = new JFrame("Recipe Manager");
    private TableRowSorter<CategoryTableModel> categorySorter;
    private TableRowSorter<IngredientTableModel> ingredientSorter;
    private TableRowSorter<RecipeTableModel> recipeSorter;
    private final JButton addButton = new JButton("Add");
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");
    private final JTable categoryTable;
    private final JTable ingredientTable;
    private final JTable recipeTable;

    private final RepositoryWrapper<Unit> unitRepository;
    private final RepositoryWrapper<BaseUnit> baseUnitRepository;
    private final JLabel totalRecipesLabel;
    private final JTextField fromField = new JTextField();
    private final JTextField toField = new JTextField();
    private final DatabaseManager databaseManager;
    private final DependencyProvider dependencyProvider;
    private int tabIndex;
    private JLabel filteredRecipesLabel;


    public MainWindow(DependencyProvider dependencyProvider) {
        this.dependencyProvider = dependencyProvider;
        databaseManager = dependencyProvider.getCreatedDatabaseManager();
        categoryTable = new CategoryTableComponent(dependencyProvider.getCategoryRepository()).getCategoryTable();
        ingredientTable = new IngredientTableComponent(dependencyProvider.getIngredientRepository()).getIngredientTable();
        recipeTable = new RecipeTableComponent(dependencyProvider.getRecipeRepository(), categoryTable.getModel(), ingredientTable.getModel()).getRecipeTable();

        setRowSorters();

        totalRecipesLabel = new JLabel("Total number of recipes: " + recipeTable.getModel().getRowCount());
        totalRecipesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        recipeTable.getModel().addTableModelListener(e -> {
            totalRecipesLabel.setText("Total number of recipes: " + recipeTable.getModel().getRowCount());
            filteredRecipesLabel.setText("Filtered number of recipes: " + recipeTable.getModel().getRowCount());
        });

        filteredRecipesLabel = new JLabel("Filtered number of recipes: " + recipeTable.getRowCount());
        recipeSorter.addRowSorterListener(e -> filteredRecipesLabel.setText("Filtered number of recipes: " + recipeTable.getRowCount()));
        filteredRecipesLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JTabbedPane tabbedPane = createTabbedPane();
        tabbedPane.addChangeListener(e -> {
            var tab = (JTabbedPane) e.getSource();
            tabIndex = tab.getSelectedIndex();

            addButton.setAction(getAddAction());
            editButton.setAction(getEditAction());
            deleteButton.setAction(getRemoveAction());
        });
        tabIndex = tabbedPane.getSelectedIndex();

        addButton.setAction(getAddAction());
        editButton.setAction(getEditAction());
        deleteButton.setAction(getRemoveAction());
        frame.add(tabbedPane, BorderLayout.CENTER);

        unitRepository = new UnitRepositoryWrapper(dependencyProvider.getUnitRepository());
        baseUnitRepository = new BaseUnitRepositoryWrapper(dependencyProvider.getBaseUnitRepository());

        categoryTable.setComponentPopupMenu(new CategoryPopupMenu(categoryTable).getPopupMenu());
        ingredientTable.setComponentPopupMenu(new IngredientPopupMenu(ingredientTable, unitRepository).getPopupMenu());
        recipeTable.setComponentPopupMenu(new RecipePopupMenu(recipeTable, (CategoryTableModel) categoryTable.getModel(), (IngredientTableModel) ingredientTable.getModel()).getPopupMenu());


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.add(new ToolBarComponent(addButton, editButton, deleteButton, totalRecipesLabel).getToolBar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());

        frame.pack();
    }

    public static JFrame getFrame() {
        return frame;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var fileMenu = new JMenu("File");

        var unitsMenuItem = new JMenuItem("Units");
        unitsMenuItem.addActionListener(this::units);
        fileMenu.add(unitsMenuItem);

        var importMenuItem = new JMenuItem("Import");
        importMenuItem.addActionListener(this::importFile);
        fileMenu.add(importMenuItem);

        var exportMenuItem = new JMenuItem("Export");
        exportMenuItem.addActionListener(this::exportFile);
        fileMenu.add(exportMenuItem);

        var exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this::exit);
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        return menuBar;
    }

    private JTabbedPane createTabbedPane() {
        var tabbedPane = new JTabbedPane();
        tabbedPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabbedPane.addTab("Recipes", createRecipeTab());
        tabbedPane.addTab("Category", createCategoryTab());
        tabbedPane.addTab("Ingredient", createIngredientTab());
        return tabbedPane;
    }

    private JPanel createRecipeTab() {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        var recipeSearchFilterPanel = new JPanel();
        recipeSearchFilterPanel.setMaximumSize(new Dimension(3096, 5));
        recipeSearchFilterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        SearchFilterPanel recipeSearchFilter = new SearchFilterPanel(recipeSorter);

        recipeSearchFilterPanel.add(recipeSearchFilter.getPanel());
        recipeSearchFilterPanel.add(new DurationFilterPanel(fromField, toField, recipeSorter).getPanel());
        var resetFilterButton = new JButton("Reset");
        resetFilterButton.addActionListener(e -> {
            recipeSearchFilter.resetTextField();
            fromField.setText("");
            toField.setText("");
        });

        recipeSearchFilterPanel.add(resetFilterButton);
        panel.add(recipeSearchFilterPanel);
        panel.add(new JScrollPane(recipeTable));
        filteredRecipesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(filteredRecipesLabel);

        return panel;
    }

    private JPanel createCategoryTab() {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        var filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        SearchFilterPanel categorySearchFilterPanel = new SearchFilterPanel(categorySorter);
        filterPanel.add(categorySearchFilterPanel.getPanel());

        var resetFilterButton = new JButton("Reset");
        resetFilterButton.addActionListener(e -> categorySearchFilterPanel.resetTextField());
        filterPanel.add(resetFilterButton);

        panel.add(filterPanel);
        panel.add(new JScrollPane(categoryTable));
        return panel;
    }

    private JPanel createIngredientTab() {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        var filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        SearchFilterPanel ingredientSearchFilterPanel = new SearchFilterPanel(ingredientSorter);
        filterPanel.add(ingredientSearchFilterPanel.getPanel());

        var resetFilterButton = new JButton("Reset");
        resetFilterButton.addActionListener(e -> ingredientSearchFilterPanel.resetTextField());
        filterPanel.add(resetFilterButton);
        panel.add(filterPanel);
        panel.add(new JScrollPane(ingredientTable));
        return panel;
    }

    private void units(ActionEvent e) {
        var dialog = new UnitsDialog(unitRepository, baseUnitRepository);
        dialog.show(null, "Units");
    }

    private void importFile(ActionEvent e) {
        var dialog = new ImportDialog();
        var importFilePath = dialog.show(null, "Import file");
        if (importFilePath.isEmpty() || importFilePath.get().equals("")) {
            return;
        }

        ImporterFormatType type = ImporterFormatType.XML;
        if (importFilePath.get().endsWith(".json")) {
            type = ImporterFormatType.JSON;
        }

        Importer importer = new AsyncImporter(
                Objects.requireNonNull(dependencyProvider.getImporter()),
                () -> {
                    refresh();
                    JOptionPane.showMessageDialog(null, "Import successfully completed.");
                }
        );

        importer.importFile(importFilePath.get(), type);
    }

    private void exportFile(ActionEvent e) {
        var dialog = new ExportDialog();
        String exportFilePath = dialog.show(null, "Export file").get();
        if (exportFilePath.equals("")) {
            return;
        }

        ExporterFormatType type = ExporterFormatType.PDF;

        if (exportFilePath.endsWith(".json")) {
            type = ExporterFormatType.JSON;
        } else if (exportFilePath.endsWith(".xml")) {
            type = ExporterFormatType.XML;
        }

        Exporter exporter = new AsyncExporter(
                dependencyProvider.getExporter(),
                () -> {
                    JOptionPane.showMessageDialog(null, "Export successfully completed.");
                });

        exporter.exportFile(exportFilePath, type);
    }

    private Action getAddAction() {
        TabType tabType = TabType.values()[tabIndex];

        return switch (tabType) {
            case CATEGORY -> new CategoryAddAction(categoryTable);
            case INGREDIENT -> new IngredientAddAction(ingredientTable, unitRepository);
            case RECIPE ->
                    new RecipeAddAction(recipeTable, (CategoryTableModel) categoryTable.getModel(), (IngredientTableModel) ingredientTable.getModel());
        };
    }

    private Action getEditAction() {
        TabType tabType = TabType.values()[tabIndex];

        return switch (tabType) {
            case CATEGORY -> new CategoryEditAction(categoryTable);
            case INGREDIENT -> new IngredientEditAction(ingredientTable, unitRepository);
            case RECIPE ->
                    new RecipeEditAction(recipeTable, (CategoryTableModel) categoryTable.getModel(), (IngredientTableModel) ingredientTable.getModel());
        };
    }

    private void setRowSorters() {
        this.categorySorter = new TableRowSorter<>((CategoryTableModel) categoryTable.getModel());
        this.ingredientSorter = new TableRowSorter<>((IngredientTableModel) ingredientTable.getModel());
        this.recipeSorter = new TableRowSorter<>((RecipeTableModel) recipeTable.getModel());

        categoryTable.setRowSorter(categorySorter);
        ingredientTable.setRowSorter(ingredientSorter);
        recipeTable.setRowSorter(recipeSorter);
    }

    private Action getRemoveAction() {
        TabType tabType = TabType.values()[tabIndex];

        return switch (tabType) {
            case CATEGORY -> new CategoryRemoveAction(categoryTable);
            case INGREDIENT -> new IngredientRemoveAction(ingredientTable);
            case RECIPE -> new RecipeRemoveAction(recipeTable);
        };
    }

    public void show() {
        frame.setVisible(true);
    }

    private void exit(ActionEvent e) {
        System.exit(0);
    }


    private void refresh() {
        baseUnitRepository.refresh();
        unitRepository.refresh();
        ((CategoryTableModel) categoryTable.getModel()).refresh();
        ((IngredientTableModel) ingredientTable.getModel()).refresh();
        ((RecipeTableModel) recipeTable.getModel()).refresh();
    }
}