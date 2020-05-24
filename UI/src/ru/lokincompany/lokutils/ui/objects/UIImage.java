package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.Texture;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;

public class UIImage extends UIObject {
    protected Texture texture;
    protected UIImageRender render;

    public UIImage(){
        render = new UIImageRender(this);
    }

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

        parent.getCanvasParent().addRenderPart(render);
    }
}

class UIImageRender extends UIRenderPart<UIImage> {

    public UIImageRender(UIImage object) {
        super(object);
    }

    @Override
    public void render() {
        Texture texture = object.getTexture();
        if (texture == null) return;

        Size size = object.getArea().getSize();
        if (size.width <= 0 || size.height <= 0)
            size = new Size(texture.getSize().getX(), texture.getSize().getY());

        object.getTexture().bind();
        GLFastTools.drawSquare(object.getArea().getRect().setSize(size));
        object.getTexture().unbind();
    }
}