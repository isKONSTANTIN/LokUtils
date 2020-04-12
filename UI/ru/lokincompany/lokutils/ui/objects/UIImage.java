package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
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

        Vector2f size = object.getSize();
        if (size.x <= 0 || size.y <= 0)
            size.set(texture.getSize().getX(), texture.getSize().getY());

        object.getTexture().bind();
        GLFastTools.drawSquare(object.getPosition(), size);
        object.getTexture().unbind();
    }
}