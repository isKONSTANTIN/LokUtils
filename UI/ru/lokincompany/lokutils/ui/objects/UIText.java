package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.render.Font;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;

import java.awt.event.TextEvent;

public class UIText extends UIObject {

    public Color overrideColor;

    protected String styleFontName;
    protected String text = "";
    protected UITextRender render;

    public UIText() {
        render = new UITextRender(this);
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
    public Vector2f getSize() {
        return getStyle().getFont(styleFontName).getSize(text, size);
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent);

        parent.getCanvasParent().addRenderPart(render);
    }
}

class UITextRender extends UIRenderPart<UIText> {

    public UITextRender(UIText panel) {
        super(panel);
    }

    @Override
    public void render() {
        Font font = object.getStyle().getFont(object.getStyleFontName());
        Color color = object.overrideColor != null ? object.overrideColor : object.getStyle().getColor("text");

        font.drawText(object.getText(), object.getPosition(), object.getSize(), color);
    }
}