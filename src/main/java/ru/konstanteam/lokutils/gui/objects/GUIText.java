package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.GUIStyle;
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
        updateSize();
    }

    public String getText() {
        return text;
    }

    public void updateSize(){
        minimumSize().set(getStyle().getFont(styleFontName).getSize(text, null));
    }

    public GUIText setText(String text) {
        this.text = text;
        updateSize();

        return this;
    }

    public String getStyleFontName() {
        return styleFontName;
    }

    public GUIText setStyleFontName(String styleFontName) {
        this.styleFontName = styleFontName;
        updateSize();

        return this;
    }

    public Color getColor() {
        return overrideColor != null ? overrideColor : getStyle().getColor("text");
    }

    public Font getFont() {
        return lastFont;
    }

    @Override
    public GUIObject setStyle(GUIStyle style) {
        super.setStyle(style);

        updateSize();
        return this;
    }

    @Override
    public void render() {
        lastFont = getStyle().getFont(getStyleFontName());
        Color color = getColor();

        lastFont.drawText(getText(), new Rect(Point.ZERO, null), color);
    }
}