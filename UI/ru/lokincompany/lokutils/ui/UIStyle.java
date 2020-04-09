package ru.lokincompany.lokutils.ui;

import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.render.Font;

import java.util.HashMap;

public class UIStyle {
    private static UIStyle defaultStyle;

    public static void generateDefaultStyle(){
        defaultStyle = new UIStyle()
                .setFont("default", new Font().load())
                .setColor("background", new Color(0.2f, 0.2f, 0.2f, 0.7f))
                .setColor("brightBackground", new Color(0.3f, 0.3f, 0.3f, 0.7f))
                .setColor("text", new Color(0.9f, 0.9f, 0.9f, 1))
                .setColor("highlightedText", new Color(1, 1, 0.9f, 1));
    }

    public static void setDefault(UIStyle style){
        defaultStyle = style;
    }

    public static UIStyle getDefault(){
        if (defaultStyle == null) throw new RuntimeException("Default style not generated or setted!");

        return defaultStyle;
    }

    protected HashMap<String, Color> colors = new HashMap<>();
    protected HashMap<String, Font> fonts = new HashMap<>();

    public Color getColor(String name){
        return colors.getOrDefault(name, new Color());
    }

    public Font getFont(String name){
        Font defaultFont = this.hashCode() != defaultStyle.hashCode() ? defaultStyle.getFont("default") : fonts.getOrDefault("default", null);

        return fonts.getOrDefault(name, defaultFont);
    }

    public UIStyle setColor(String name, Color color){
        colors.put(name, color);

        return this;
    }

    public UIStyle setFont(String name, Font font){
        fonts.put(name, font);

        return this;
    }


}