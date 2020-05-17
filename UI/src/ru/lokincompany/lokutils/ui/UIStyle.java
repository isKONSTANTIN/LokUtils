package ru.lokincompany.lokutils.ui;

import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.render.Font;
import ru.lokincompany.lokutils.render.GLContext;

import java.util.HashMap;

public class UIStyle {
    private static HashMap<GLContext, UIStyle> defaultStyles = new HashMap<>();
    protected HashMap<String, Color> colors = new HashMap<>();
    protected HashMap<String, Font> fonts = new HashMap<>();

    public static void generateDefaultStyle() {
        if (GLContext.getCurrent() == null)
            throw new RuntimeException("Default style cannot be generated without OpenGL context!");

        setDefault(
                new UIStyle()
                        .setFont("default", new Font().load())
                        .setColor("background", new Color(0.2f, 0.2f, 0.2f, 0.7f))
                        .setColor("buttonPressed", new Color(0.4f, 0.4f, 0.4f, 1f))
                        .setColor("buttonPointed", new Color(0.35f, 0.35f, 0.35f, 1f))
                        .setColor("buttonBackground", new Color(0.3f, 0.3f, 0.3f, 1f))
                        .setColor("brightBackground", new Color(0.3f, 0.3f, 0.3f, 0.7f))
                        .setColor("text", new Color(0.9f, 0.9f, 0.9f, 1))
                        .setColor("highlightedText", new Color(1, 1, 0.9f, 1))
                        .setColor("checkboxStroke", new Color(0.9f, 0.9f, 0.9f, 1))
                        .setColor("checkboxFillActive", new Color(0.8f, 0.8f, 0.8f, 1))
                        .setColor("checkboxFillInactive", new Color(0.5f, 0.5f, 0.5f, 1))
        );
    }

    public static UIStyle getDefault() {
        GLContext context = GLContext.getCurrent();

        if (!defaultStyles.containsKey(context))
            generateDefaultStyle();

        return defaultStyles.get(context);
    }

    public static void setDefault(UIStyle style) {
        defaultStyles.put(GLContext.getCurrent(), style);
    }

    public Color getColor(String name) {
        return colors.getOrDefault(name, new Color());
    }

    public Font getFont(String name) {
        Font defaultFont = fonts.get("default");

        return fonts.getOrDefault(name, defaultFont);
    }

    public UIStyle setColor(String name, Color color) {
        colors.put(name, color);

        return this;
    }

    public UIStyle setFont(String name, Font font) {
        fonts.put(name, font);

        return this;
    }


}
