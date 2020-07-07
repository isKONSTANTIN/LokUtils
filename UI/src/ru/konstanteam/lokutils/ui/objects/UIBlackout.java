package ru.konstanteam.lokutils.ui.objects;

import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.ui.UIObject;

import static org.lwjgl.opengl.GL11.glColor4f;

public class UIBlackout extends UIObject {
    public Color overrideColor;
    protected float rounded;

    public UIBlackout() {
    }

    public UIBlackout(float rounded) {
        setRounded(rounded);
    }

    public float getRounded() {
        return rounded;
    }

    public void setRounded(float rounded) {
        this.rounded = Math.max(Math.min(rounded, 1), 0);
    }

    @Override
    public void render() {
        Color color = overrideColor != null ? overrideColor : getStyle().getColor("background");
        glColor4f(color.red, color.green, color.blue, color.alpha);
        GLFastTools.drawRoundedSquare(new Rect(size.get()), rounded);
    }
}
