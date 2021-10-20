package su.knst.lokutils.gui.objects;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.Texture;
import su.knst.lokutils.render.tools.GLFastTools;

public class GUIImage extends GUIObject {
    protected Texture texture;

    public Texture getTexture() {
        return texture;
    }

    public GUIImage setTexture(Texture texture) {
        boolean overrideSize = this.texture == null;

        this.texture = texture;

        if (overrideSize)
            minimumSize().set(new Size(texture.getSize().getX(), texture.getSize().getY()));

        return this;
    }

    @Override
    public void render() {
        if (texture == null) return;

        GLFastTools.drawTexturedSquare(new Rect(Point.ZERO, size().get()), texture);
    }
}