package ru.lokincompany.lokutils.ui;

import ru.lokincompany.lokutils.objects.Color;

import java.util.HashMap;

public class UIStyle {
    public static UIStyle defaultStyle;

    static {
        defaultStyle = new UIStyle()
                .setColor("background", new Color(0.2f, 0.2f, 0.2f, 0.7f))
                .setColor("brightBackground", new Color(0.3f, 0.3f, 0.3f, 0.7f))
                .setColor("text", new Color(0.9f, 0.9f, 0.9f, 1))
                .setColor("highlightedText", new Color(1, 1, 0.9f, 1));
    }

    protected HashMap<String, Color> colors = new HashMap<>();

    public Color getColor(String name){
        return colors.getOrDefault(name, new Color());
    }

    public UIStyle setColor(String name, Color color){
        colors.put(name, color);

        return this;
    }

}
