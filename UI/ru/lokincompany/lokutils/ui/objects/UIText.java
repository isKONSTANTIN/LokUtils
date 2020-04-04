package ru.lokincompany.lokutils.ui.objects;

import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.render.Font;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

public class UIText extends UIObject {

    protected String styleFontName;
    protected String text;
    protected UITextRender render;

    public UIText(){
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
    public RenderPart update(UICanvas parent) {
        super.update(parent);
        return render;
    }
}

class UITextRender extends UIRenderPart<UIText> {

    public UITextRender(UIText panel) {
        super(panel);
    }

    @Override
    public void render() {
        Font font = object.getStyle().getFont(object.getStyleFontName() != null ? object.getStyleFontName() : "default");
        Color color = object.getStyle().getColor("text");

        font.drawText(object.getText(), object.getPosition(), object.getSize(), color);
    }
}