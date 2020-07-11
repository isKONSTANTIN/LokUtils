package ru.konstanteam.lokutils.ui.layout;

import ru.konstanteam.lokutils.input.Inputs;
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
    protected Inputs inputs;
    protected boolean positionsIsValid;
    protected UIObject focusedObject;

    public UIAbstractLayout(Inputs inputs, UIStyle style) {
        this.inputs = inputs;
        this.style = style;
        this.size.set(new Size(256, 256));
        this.size.addListener((oldValue, newValue) -> setInvalidPositionsStatus());

        customersContainer.addCustomer(event -> {
            if (event instanceof MouseClickedEvent)
                focusedObject = null;

            for (UIObject object : objects){
                Event localizedEvent = event.relativeTo(getObjectPos(object));

                if (event instanceof MouseClickedEvent){
                    MouseClickedEvent mouseClickedEvent = (MouseClickedEvent)localizedEvent;
                    Size objectSize = object.size().get();

                    if (mouseClickedEvent.position.x >= 0 && mouseClickedEvent.position.y >= 0 && mouseClickedEvent.position.x <= objectSize.width && mouseClickedEvent.position.y <= objectSize.height)
                        focusedObject = object.getFocusableObject();
                }

                object.getCustomersContainer().handle(localizedEvent);
            }


        }, Event.class);
    }

    public UIAbstractLayout(Inputs inputs) {
        this(inputs, UIStyle.getDefault());
    }

    public UIAbstractLayout() {
        this(GLContext.getCurrent().getWindow().getInputs());
    }

    public UIObject getFocusedObject(){
        return focusedObject;
    }

    public boolean isFocused(UIObject object){
        return focusedObject != null && focusedObject.equals(object);
    }

    @Override
    public Inputs getInputs() {
        return inputs;
    }

    @Override
    public UIAbstractLayout getOwner() {
        return this;
    }

    protected abstract Point getObjectPos(UIObject object);

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
        Size mySize = size().get();

        viewTools.pushLook(new Rect(0, 0, mySize.width, mySize.height));

        for (UIObject object : objects) {
            Point objectPosition = getObjectPos(object);
            Size objectSize = object.size().get();

            viewTools.pushLook(new Rect(objectPosition.x, objectPosition.y, objectSize.width, objectSize.height));
            object.render();
            viewTools.popLook();
        }

        viewTools.popLook();
    }

}