package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.style.GUIStyle;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.render.tools.GUIRenderBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public class GUISeparate extends GUIObject {
    protected boolean horizontal;
    protected boolean autoMode = true;
    protected float lineSizePercent = 0.85f;

    public GUISeparate() {
        size().set(new Size(100, 11));
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isAutoMode() {
        return autoMode;
    }

    public void setAutoMode(boolean autoMode) {
        this.autoMode = autoMode;
    }

    public float getLineSizePercent() {
        return lineSizePercent;
    }

    public void setLineSizePercent(float lineSizePercent) {
        this.lineSizePercent = lineSizePercent;
    }

    @Override
    public void render() {
        Size size = size().get();

        boolean horizontal = autoMode ? size.width > size.height : this.horizontal;
        float lineSize = (horizontal ? size.width : size.height) * lineSizePercent;
        float lineWidth = (Float) asset.object("lineWidth");

        Color lineColor = asset.color("color");
        glDisable(GL_MULTISAMPLE);

        glLineWidth(lineWidth);
        glColor4f(lineColor.red, lineColor.green, lineColor.blue, lineColor.alpha);

        GUIRenderBuffer buffer = GLContext.getCurrent().getViewTools().getGuiRenderBuffer();
        buffer.begin();

        if (horizontal) {
            buffer.addVertex(size.width / 2f - lineSize / 2f, size.height / 2f - lineWidth / 2f - 1);
            buffer.addVertex(size.width / 2f - lineSize / 2f + lineSize, size.height / 2f - lineWidth / 2f - 1);
        } else {
            buffer.addVertex(size.width / 2f - lineWidth / 2f, size.height / 2f - lineSize / 2f);
            buffer.addVertex(size.width / 2f - lineWidth / 2f, size.height / 2f - lineSize / 2f + lineSize);
        }

        buffer.draw(GL_LINES);

        glEnable(GL_MULTISAMPLE);
    }
}
