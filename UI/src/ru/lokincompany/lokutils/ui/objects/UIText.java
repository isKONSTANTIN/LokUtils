package ru.lokincompany.lokutils.ui.objects;

import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.Font;
import ru.lokincompany.lokutils.tools.property.Property;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.positioning.AdvancedRect;

public class UIText extends UIObject {
    public Color overrideColor;

    protected String styleFontName;
    protected String text = "";

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

    @Override
    public void update(UIObject parent) {
        super.update(parent);
    }

    @Override
    public void render() {
        Font font = getStyle().getFont(getStyleFontName());
        Color color = overrideColor != null ? overrideColor : getStyle().getColor("text");

        font.drawText(getText(), new Rect(Point.ZERO, size.get()), color);
    }
}