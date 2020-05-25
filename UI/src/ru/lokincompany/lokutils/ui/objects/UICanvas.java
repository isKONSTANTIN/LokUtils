package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;
import ru.lokincompany.lokutils.ui.UIStyle;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.EventHandler;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.lokincompany.lokutils.ui.eventsystem.events.MousePointedEvent;
import sun.security.util.math.intpoly.P256OrderField;

import java.util.ArrayList;
import java.util.Vector;

public class UICanvas extends UIObject {

    protected final Object updateSync = new Object();
    protected Vector<UIObject> objects = new Vector<>();
    protected Vector<RenderPart> renderParts = new Vector<>();
    protected UICanvasRender render;
    protected Inputs inputs;

    public UICanvas(Inputs inputs, UIStyle style) {
        this.inputs = inputs;
        this.style = style;
        render = new UICanvasRender(this);
        setSize(new Size(256, 256));

        customersContainer.addCustomer(event -> {

            for (UIObject object : objects) {
                Point newMouseClickPosition = event.position.relativeTo(area.getX(), area.getY());

                if (!object.getArea().getRect().inside(newMouseClickPosition)) continue;

                object.getCustomersContainer().handle(
                        new MouseClickedEvent(newMouseClickPosition, event.clickType, event.button)
                );
            }

        }, MouseClickedEvent.class);

    }

    public UICanvas(Inputs inputs) {
        this(inputs, UIStyle.getDefault());
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

    public <T extends UIObject> T getObject(Class<T> objectType, String name) {
        for (UIObject object : objects) {
            if (object.getName().equals(name) && objectType.isInstance(object))
                return (T)object;
        }

        return null;
    }

    public <T extends UIObject> T getObject(Class<T> objectType) {
        for (UIObject object : objects) {
            if (objectType.isInstance(object))
                return (T)object;
        }

        return null;
    }

    public UIObject getObject(String name) {
        for (UIObject object : objects) {
            if (object.getName().equals(name))
                return object;
        }

        return null;
    }

    public UICanvas addObject(UIObject object) {
        object.init(this);
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
        renderParts.add(renderPart);
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent != null ? parent : this);

        renderParts.clear();

        for (UIObject object : objects) {
            try {
                object.update(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
        GLContext.getCurrent().getViewTools().pushLook(object.getArea().getRect());

        for (RenderPart renderPart : object.renderParts)
            renderPart.render();

        GLContext.getCurrent().getViewTools().popLook();
    }
}