package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.render.Font;

public class GUIText extends GUIObject {
    public Color overrideColor;

    protected String styleFontName;
    protected String text = "";
    protected Font lastFont;

    public GUIText() {
        minimumSize().set(() -> getStyle().getFont(styleFontName).getSize(text, null));
    }

    public String getText() {
        return text;
    }

    public GUIText setText(String text) {
        this.text = text;

        return this;
    }

    public String getStyleFontName() {
        return styleFontName;
    }

    public GUIText setStyleFontName(String styleFontName) {
        this.styleFontName = styleFontName;

        return this;
    }

    public Color getColor() {
        return overrideColor != null ? overrideColor : getStyle().getColor("text");
    }

    public Font getFont() {
        return lastFont;
    }

    @Override
    public void render() {
        lastFont = getStyle().getFont(getStyleFontName());
        Color color = getColor();

        lastFont.drawText(getText(), new Rect(Point.ZERO, null), color);
    }
}