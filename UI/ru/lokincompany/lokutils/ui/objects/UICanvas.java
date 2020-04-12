package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;

import java.util.ArrayList;
import java.util.Vector;

public class UICanvas extends UIObject {

    protected final Object updateSync = new Object();
    protected Vector<UIObject> objects = new Vector<>();
    protected Vector<RenderPart> renderParts = new Vector<>();
    protected ArrayList<RenderPart> additionalRenderList = new ArrayList<>();
    protected UICanvasRender render;
    protected Inputs inputs;

    public UICanvas(Inputs inputs) {
        this.inputs = inputs;
        render = new UICanvasRender(this);
        setSize(new Vector2f(256, 256));
    }

    public UICanvas() {
        this(GLContext.getCurrent().getWindow().getInputs());
    }

    @Override
    public Inputs getInputs() {
        return inputs;
    }

    @Override
    public UICanvas getCanvasParent() {
        return this;
    }

    public UIObject getObject(String name) {
        for (UIObject object : objects) {
            if (object.getName().equals(name))
                return object;
        }

        return null;
    }

    public UICanvas addObject(UIObject object) {
        objects.add(object);
        return this;
    }

    public boolean removeObject(String name) {
        for (int i = 0; i < objects.size(); i++) {
            UIObject object = objects.get(i);

            if (object.getName().equals(name)) {
                objects.remove(i);
                return true;
            }

        }
        return false;
    }

    public void addRenderPart(RenderPart renderPart) {
        additionalRenderList.add(renderPart);
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent != null ? parent : this);

        for (UIObject object : objects) {
            try {
                object.update(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        synchronized (updateSync) {
            renderParts.clear();
            renderParts.addAll(additionalRenderList);
        }

        additionalRenderList.clear();

        if (parent != null)
            parent.getCanvasParent().addRenderPart(render);
    }
}

class UICanvasRender extends UIRenderPart<UICanvas> {

    public UICanvasRender(UICanvas object) {
        super(object);
    }

    @Override
    public void render() {
        ViewTools.moveOrtho2DView(object.getPosition().x, object.getPosition().y);

        synchronized (object.updateSync) {
            for (RenderPart renderPart : object.renderParts)
                renderPart.render();
        }
        ViewTools.moveOrtho2DView(-object.getPosition().x, -object.getPosition().y);
    }
}