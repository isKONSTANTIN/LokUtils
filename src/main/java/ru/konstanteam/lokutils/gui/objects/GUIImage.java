package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Texture;
import ru.konstanteam.lokutils.render.tools.GLFastTools;

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