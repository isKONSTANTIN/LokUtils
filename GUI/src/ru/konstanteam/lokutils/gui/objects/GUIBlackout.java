package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.gui.GUIObject;

import static org.lwjgl.opengl.GL11.glColor4f;

public class GUIBlackout extends GUIObject {
    public Color overrideColor;
    protected float rounded;

    public GUIBlackout() {
        size().set(new Size(-1, -1));
    }

    public GUIBlackout(float rounded) {
        setRounded(rounded);
    }

    public float getRounded() {
        return rounded;
    }

    public void setRounded(float rounded) {
        this.rounded = Math.max(Math.min(rounded, 1), 0);
    }

    @Override
    public void update() {
        super.update();

        if (size().get().height == -1 || size().get().width == -1)
            size().set(owner.size());
    }

    @Override
    public void render() {
        Color color = overrideColor != null ? overrideColor : getStyle().getColor("background");
        glColor4f(color.red, color.green, color.blue, color.alpha);
        GLFastTools.drawRoundedSquare(new Rect(size.get()), rounded);
    }
}
