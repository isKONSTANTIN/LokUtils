package su.knst.lokutils.gui.objects;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.render.text.AbstractFont;
import su.knst.lokutils.render.text.TextRenderHelper;
import su.knst.lokutils.tools.property.PropertyBasic;

public class GUIText extends GUIObject {
    public Color overrideColor;

    protected String styleFontName = "default";
    protected PropertyBasic<String> text = new PropertyBasic<>("");
    protected AbstractFont lastFont;

    public GUIText(String text){
        this.text.set(text);
        minimumSize().track(() -> TextRenderHelper.getSize(asset.font(styleFontName), this.text.get(), null));
        maximumSize().track(minimumSize());
    }

    public GUIText() {
        this("GUIText");
    }

    public PropertyBasic<String> string(){
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

    public AbstractFont getFont() {
        return lastFont;
    }

    @Override
    public void render() {
        lastFont = asset.font(getStyleFontName());
        Color color = getColor();

        TextRenderHelper.drawText(lastFont, text.get(), new Rect(Point.ZERO, null), color);
    }
}