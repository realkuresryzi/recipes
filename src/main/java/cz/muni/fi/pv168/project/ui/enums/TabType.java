package cz.muni.fi.pv168.project.ui.enums;

public enum TabType {
    RECIPE {
        @Override
        public String toString() {
            return "Recipes";
        }
    },
    CATEGORY {
        @Override
        public String toString() {
            return "Categories";
        }
    },
    INGREDIENT {
        @Override
        public String toString() {
            return "Ingredients";
        }
    }
}