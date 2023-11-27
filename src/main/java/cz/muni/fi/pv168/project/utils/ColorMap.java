package cz.muni.fi.pv168.project.utils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorMap {

    public HashMap<Color, String> colorMap = new HashMap<>();

    // init with 16 colors
    public ColorMap() {
        colorMap.put(Color.RED, "Red");
        colorMap.put(Color.BLUE, "Blue");
        colorMap.put(Color.GREEN, "Green");
        colorMap.put(Color.YELLOW, "Yellow");
        colorMap.put(Color.ORANGE, "Orange");
        colorMap.put(Color.PINK, "Pink");
        colorMap.put(Color.GRAY, "Gray");
        colorMap.put(Color.CYAN, "Cyan");
        colorMap.put(Color.MAGENTA, "Magenta");
    }

    public String[] getColorsNames() {
        return colorMap.values().toArray(new String[0]);
    }

    public String getColorString(Color color) {
        return colorMap.get(color);
    }

    public Color getColorByName(String name) {
        for (Map.Entry<Color, String> set : colorMap.entrySet()) {
            if (set.getValue() == name) {
                return set.getKey();
            }
        }
        return Color.RED;
    }


}
