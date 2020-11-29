package ru.konstanteam.lokutils.gui.layout;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.GUIStyle;
import ru.konstanteam.lokutils.gui.eventsystem.Event;
import ru.konstanteam.lokutils.gui.eventsystem.events.ClickType;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.render.tools.ViewTools;
import ru.konstanteam.lokutils.tools.Removable;
import ru.konstanteam.lokutils.tools.property.PropertyChangeListener;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GUIAbstractLayout extends GUIObject {
    protected ArrayList<GUIObject> objects = new ArrayList<>();
    protected boolean isValid;
    protected GUIObject focusedObject;
    protected int refreshRate = 60;
    protected PropertyChangeListener invalidListener;

    public GUIAbstractLayout(GUIStyle style) {
        this.style = style;
        this.invalidListener = (oldValue, newValue) -> setInvalidStatus();
        this.size.set(new Size(256, 256));
        this.size.addListener(invalidListener);

        customersContainer.addCustomer(event -> {
            if (event instanceof MouseClickedEvent) {
                MouseClickedEvent mouseClickedEvent = (MouseClickedEvent) event;

                if (mouseClickedEvent.clickType == ClickType.CLICKED || !(new Rect(Point.ZERO, size.get()).inside(mouseClickedEvent.position)))
                    focusedObject = null;
            }

            for (GUIObject object : objects) {
                Point position = getObjectPos(object);
                position = position != null ? position : Point.ZERO;

                Event localizedEvent = event.relativeTo(position);

                if (event instanceof MouseClickedEvent) {
                    MouseClickedEvent mouseClickedEvent = (MouseClickedEvent) localizedEvent;
                    Size objectSize = object.size().get();

                    if (new Rect(Point.ZERO, objectSize).inside(mouseClickedEvent.position) && mouseClickedEvent.clickType == ClickType.CLICKED)
                        focusedObject = object.getFocusableObject();
                }

                object.getCustomersContainer().handle(localizedEvent);
            }
        });
    }

    public GUIAbstractLayout() {
        this(GUIStyle.getDefault());
    }

    public GUIObject getFocusedObject() {
        return focusedObject;
    }

    public boolean isFocused(GUIObject object) {
        return focusedObject != null && focusedObject.equals(object);
    }

    public void setFocus(GUIObject object) {
        focusedObject = object.getFocusableObject();
    }

    @Override
    public GUIAbstractLayout getOwner() {
        return this;
    }

    protected abstract Point getObjectPos(GUIObject object);

    protected void addObject(GUIObject object) {
        object.init(this);
        objects.add(object);

        object.size().addListener(invalidListener);
        object.minimumSize().addListener(invalidListener);
        object.maximumSize().addListener(invalidListener);
    }

    protected boolean removeObject(GUIObject object) {
        boolean result = objects.remove(object);

        if (result) {
            object.size().removeListener(invalidListener);
            object.minimumSize().removeListener(invalidListener);
            object.maximumSize().removeListener(invalidListener);

            setInvalidStatus();
        }

        return result;
    }

    protected void removeAll() {
        for (GUIObject object : objects) {
            object.size().removeListener(invalidListener);
            object.minimumSize().removeListener(invalidListener);
            object.maximumSize().removeListener(invalidListener);
        }

        objects.clear();

        setInvalidStatus();
    }

    protected abstract void calculateAll();

    protected void setInvalidStatus() {
        isValid = false;
    }

    @Override
    public void update() {
        for (GUIObject object : objects) {
            try {
                object.update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!isValid) {
            calculateAll();

            isValid = true;
        }
    }

    public int getRefreshRate() {
        return owner != null ? owner.getRefreshRate() : refreshRate;
    }

    @Override
    public void render() {
        refreshRate = GLContext.getCurrent().getWindow().getMonitor().getVideoMode().refreshRate();

        ViewTools viewTools = GLContext.getCurrent().getViewTools();

        for (GUIObject object : objects) {
            Point objectPosition = getObjectPos(object);

            viewTools.pushTranslate(objectPosition);

            object.render();

            viewTools.popTranslate();
        }
    }

}