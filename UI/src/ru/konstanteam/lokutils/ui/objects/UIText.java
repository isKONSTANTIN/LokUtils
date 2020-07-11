package ru.konstanteam.lokutils.ui.objects;

import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.render.Font;
import ru.konstanteam.lokutils.ui.UIObject;

public class UIText extends UIObject {
    public Color overrideColor;

    protected String styleFontName;
    protected String text = "";
    protected Font lastFont;

    public UIText() {
        size.set(() -> getStyle().getFont(styleFontName).getSize(text, null));
    }

    public String getText() {
        return text;
    }

    public UIText setText(String text) {
        this.text = text;

        return this;
    }

    public String getStyleFontName() {
        return styleFontName;
    }

    public UIText setStyleFontName(String styleFontName) {
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
    public void update(UIObject parent) {
        super.update(parent);
    }

    @Override
    public void render() {
        lastFont = getStyle().getFont(getStyleFontName());
        Color color = getColor();

        lastFont.drawText(getText(), new Rect(Point.ZERO, size.get()), color);
    }
}