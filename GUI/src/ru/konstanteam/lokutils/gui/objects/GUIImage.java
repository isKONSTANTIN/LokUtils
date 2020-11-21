package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Texture;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.gui.GUIObject;

public class GUIImage extends GUIObject {
    protected Texture texture;

    public Texture getTexture() {
        return texture;
    }

    public GUIImage setTexture(Texture texture) {
        this.texture = texture;

        return this;
    }

    @Override
    public void render() {
        Texture texture = getTexture();
        if (texture == null) return;

        Size size = size().get();
        if (size.width <= 0 || size.height <= 0)
            size = new Size(texture.getSize().getX(), texture.getSize().getY());

        texture.bind();
        GLFastTools.drawSquare(new Rect(Point.ZERO, size));
        texture.unbind();
    }
}