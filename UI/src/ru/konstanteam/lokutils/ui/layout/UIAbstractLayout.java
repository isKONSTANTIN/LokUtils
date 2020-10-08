package ru.konstanteam.lokutils.ui.layout;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.render.tools.ViewTools;
import ru.konstanteam.lokutils.tools.Removable;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.UIStyle;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseClickedEvent;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class UIAbstractLayout extends UIObject {
    protected ArrayList<UIObject> objects = new ArrayList<>();
    protected HashMap<UIObject, Removable> listeners = new HashMap<>();
    protected boolean positionsIsValid;
    protected UIObject focusedObject;

    public UIAbstractLayout(UIStyle style) {
        this.style = style;
        this.size.set(new Size(256, 256));
        this.size.addListener((oldValue, newValue) -> setInvalidPositionsStatus());

        customersContainer.addCustomer(event -> {
            if (event instanceof MouseClickedEvent)
                focusedObject = null;

            for (UIObject object : objects) {
                Point position = getObjectPos(object);
                position = position != null ? position : Point.ZERO;

                Event localizedEvent = event.relativeTo(position);

                if (event instanceof MouseClickedEvent) {
                    MouseClickedEvent mouseClickedEvent = (MouseClickedEvent) localizedEvent;
                    Size objectSize = object.size().get();

                    if (mouseClickedEvent.position.x >= 0 && mouseClickedEvent.position.y >= 0 && mouseClickedEvent.position.x <= objectSize.width && mouseClickedEvent.position.y <= objectSize.height)
                        focusedObject = object.getFocusableObject();
                }

                object.getCustomersContainer().handle(localizedEvent);
            }
        }, Event.class);
    }

    public UIAbstractLayout() {
        this(UIStyle.getDefault());
    }

    public UIObject getFocusedObject() {
        return focusedObject;
    }

    public boolean isFocused(UIObject object) {
        return focusedObject != null && focusedObject.equals(object);
    }

    public void setFocus(UIObject object) {
        focusedObject = object.getFocusableObject();
    }

    @Override
    public UIAbstractLayout getOwner() {
        return this;
    }

    protected abstract Point getObjectPos(UIObject object);

    protected abstract Point getLazyObjectPos(UIObject object);

    protected void addObject(UIObject object) {
        object.init(this);
        objects.add(object);
        listeners.put(object,
                object.size().addListener((oldValue, newValue) -> {
                    setInvalidPositionsStatus();
                })
        );
    }

    protected boolean removeObject(UIObject object) {
        boolean result = objects.remove(object);

        if (result) {
            listeners.get(object).delete();
            listeners.remove(object);

            setInvalidPositionsStatus();
        }

        return result;
    }

    protected void removeAll() {
        for (UIObject object : objects) {
            listeners.get(object).delete();
            listeners.remove(object);
        }

        objects.clear();

        setInvalidPositionsStatus();
    }

    public boolean tryRemoveObject(UIObject object) {
        if (!object.isPublicRemovableObject())
            return false;

        return removeObject(object);
    }

    protected abstract void calculateAll();

    protected void setInvalidPositionsStatus() {
        positionsIsValid = false;
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent != null ? parent : this);

        if (!positionsIsValid)
            calculateAll();

        for (UIObject object : objects) {
            try {
                object.update(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render() {
        ViewTools viewTools = GLContext.getCurrent().getViewTools();

        for (UIObject object : objects) {
            Point objectPosition = getObjectPos(object);

            viewTools.pushTranslate(objectPosition);

            object.render();

            viewTools.popTranslate();
        }
    }

}