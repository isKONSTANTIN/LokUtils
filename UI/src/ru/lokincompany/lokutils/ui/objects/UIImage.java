package ru.lokincompany.lokutils.ui.objects;

import com.sun.org.apache.regexp.internal.RE;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.Texture;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.ui.UIObject;

public class UIImage extends UIObject {
    protected Texture texture;

    public Texture getTexture() {
        return texture;
    }

    public UIImage setTexture(Texture texture) {
        this.texture = texture;

        return this;
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent);
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