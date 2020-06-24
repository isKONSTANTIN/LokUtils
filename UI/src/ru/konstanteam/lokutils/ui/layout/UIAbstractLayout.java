package ru.konstanteam.lokutils.ui.layout;

import ru.konstanteam.lokutils.input.Inputs;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.render.tools.ViewTools;
import ru.konstanteam.lokutils.tools.Removable;
import ru.konstanteam.lokutils.tools.property.Property;
import ru.konstanteam.lokutils.tools.property.PropertyChangeListener;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.UIStyle;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public abstract class UIAbstractLayout extends UIObject {
    protected ArrayList<UIObject> objects = new ArrayList<>();
    protected HashMap<UIObject, Removable> listeners = new HashMap<>();
    protected Inputs inputs;
    protected boolean positionsIsValid;

    public UIAbstractLayout(Inputs inputs, UIStyle style) {
        this.inputs = inputs;
        this.style = style;
        this.size.set(new Size(256, 256));
        this.size.addListener((oldValue, newValue) -> setInvalidPositionsStatus());

        customersContainer.addCustomer(event -> {
            for (UIObject object : objects) {
                Point objectPosition = getObjectPosition(object);
                Point newMouseClickPosition = event.position.relativeTo(objectPosition);

                object.getCustomersContainer().handle(
                        new MouseClickedEvent(newMouseClickPosition, event.clickType, event.button)
                );
            }
        }, MouseClickedEvent.class);

        customersContainer.addCustomer(event -> {
            for (UIObject object : objects) {
                Point objectPosition = getObjectPosition(object);

                Point newStartPosition = event.startPosition.relativeTo(objectPosition);
                Point newLastPosition = event.lastPosition.relativeTo(objectPosition);
                Point newEndPosition = event.endPosition.relativeTo(objectPosition);

                object.getCustomersContainer().handle(
                        new MouseMoveEvent(newStartPosition, newLastPosition, newEndPosition, event.type)
                );
            }
        }, MouseMoveEvent.class);
    }

    public UIAbstractLayout(Inputs inputs) {
        this(inputs, UIStyle.getDefault());
    }

    public UIAbstractLayout() {
        this(GLContext.getCurrent().getWindow().getInputs());
    }

    @Override
    public Inputs getInputs() {
        return inputs;
    }

    @Override
    public UIAbstractLayout getOwner() {
        return this;
    }

    protected abstract Point getObjectPosition(UIObject object);

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

        if (result){
            listeners.get(object).delete();
            listeners.remove(object);

            setInvalidPositionsStatus();
        }

        return result;
    }

    protected abstract void calculateAll();

    protected void setInvalidPositionsStatus(){
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
        Size mySize = size().get();

        viewTools.pushLook(new Rect(0,0, mySize.width, mySize.height));

        for (UIObject object : objects){
            Point objectPosition = getObjectPosition(object);
            Size objectSize = object.size().get();

            viewTools.pushLook(new Rect(objectPosition.x, objectPosition.y, objectSize.width, objectSize.height));
            object.render();
            viewTools.popLook();
        }

        viewTools.popLook();
    }

}