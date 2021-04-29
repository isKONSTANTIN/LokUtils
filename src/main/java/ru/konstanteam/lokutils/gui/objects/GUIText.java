package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.style.GUIStyle;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.render.text.Font;
import ru.konstanteam.lokutils.render.text.TextRenderHelper;
import ru.konstanteam.lokutils.tools.property.Property;
import ru.konstanteam.lokutils.tools.property.PropertyChangeListener;

public class GUIText extends GUIObject {
    public Color overrideColor;

    protected String styleFontName = "default";
    protected Property<String> text = new Property<>("");
    protected Font lastFont;

    public GUIText(String text){
        this.text.set(text);
        minimumSize().set(() -> TextRenderHelper.getSize(asset.font(styleFontName), this.text.get(), null));
        maximumSize().set(minimumSize());
    }

    public GUIText() {
        this("GUIText");
    }

    public Property<String> string(){
        return text;
    }

    public String getStyleFontName() {
        return styleFontName;
    }

    public GUIText setStyleFontName(String styleFontName) {
        this.styleFontName = styleFontName;

        return this;
    }

    public Color getColor() {
        return overrideColor != null ? overrideColor : asset.color("text");
    }

    public Font getFont() {
        return lastFont;
    }

    @Override
    public void render() {
        lastFont = asset.font(getStyleFontName());
        Color color = getColor();

        TextRenderHelper.drawText(lastFont, text.get(), new Rect(Point.ZERO, null), color);
    }
}