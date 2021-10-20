package su.knst.lokutils.gui.objects;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.render.tools.GLFastTools;

import static org.lwjgl.opengl.GL11.glColor4f;

public class GUIBlackout extends GUIObject {
    public Color overrideColor;
    protected float rounded;

    public GUIBlackout() {
        minimumSize().track(() -> owner.size().get());
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
    public void render() {
        Color color = overrideColor != null ? overrideColor : asset.color("background");
        glColor4f(color.red, color.green, color.blue, color.alpha);
        GLFastTools.drawRoundedSquare(new Rect(size.get()), rounded);
    }
}
