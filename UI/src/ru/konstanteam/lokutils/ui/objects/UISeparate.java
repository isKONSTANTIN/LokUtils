package ru.konstanteam.lokutils.ui.objects;

import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.UIStyle;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public class UISeparate extends UIObject {
    protected boolean horizontal;
    protected boolean autoMode = true;
    protected float lineSizePercent = 0.85f;

    public UISeparate() {
        size.set(new Size(100, 10));
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
        UIStyle style = getStyle();

        boolean horizontal = autoMode ? size.width > size.height : this.horizontal;
        float lineSize = (horizontal ? size.width : size.height) * lineSizePercent;
        float lineWidth = (Float) style.getObject("separateLineWidth");

        Color lineColor = style.getColor("separateColor");
        glDisable(GL_MULTISAMPLE);

        glLineWidth(lineWidth);
        glColor4f(lineColor.red, lineColor.green, lineColor.blue, lineColor.alpha);
        glBegin(GL_LINES);

        if (horizontal) {
            glVertex2d(size.width / 2f - lineSize / 2f, size.height / 2f - lineWidth / 2f);
            glVertex2d(size.width / 2f - lineSize / 2f + lineSize, size.height / 2f - lineWidth / 2f);
        } else {
            glVertex2d(size.width / 2f - lineWidth / 2f, size.height / 2f - lineSize / 2f);
            glVertex2d(size.width / 2f - lineWidth / 2f, size.height / 2f - lineSize / 2f + lineSize);
        }

        glEnd();

        glEnable(GL_MULTISAMPLE);
    }
}
