package ru.lokincompany.lokutils.ui.objects;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.tools.property.Property;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIStyle;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseMoveEvent;
import ru.lokincompany.lokutils.ui.positioning.PositioningAlgorithm;
import ru.lokincompany.lokutils.ui.positioning.PositioningLink;

import java.util.HashMap;
import java.util.Vector;

public class UICanvas extends UIObject {
    protected Vector<UIObject> objects = new Vector<>();
    protected HashMap<UIObject, Property<Point>> objectsPositions = new HashMap<>();
    protected Inputs inputs;

    public UICanvas(Inputs inputs, UIStyle style) {
        this.inputs = inputs;
        this.style = style;
        size.set(new Size(256, 256));

        customersContainer.addCustomer(event -> {

            for (UIObject object : objects) {
                Point objectPosition = objectsPositions.get(object).get();
                Point newMouseClickPosition = event.position.relativeTo(objectPosition);

                object.getCustomersContainer().handle(
                        new MouseClickedEvent(newMouseClickPosition, event.clickType, event.button)
                );
            }

        }, MouseClickedEvent.class);

        customersContainer.addCustomer(event -> {

            for (UIObject object : objects) {
                Point objectPosition = objectsPositions.get(object).get();

                Point newStartPosition = event.startPosition.relativeTo(objectPosition);
                Point newLastPosition = event.lastPosition.relativeTo(objectPosition);
                Point newEndPosition = event.endPosition.relativeTo(objectPosition);

                object.getCustomersContainer().handle(
                        new MouseMoveEvent(newStartPosition, newLastPosition, newEndPosition, event.type)
                );
            }

        }, MouseMoveEvent.class);
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

    protected Property<Point> getNewObjectPosition(UIObject object){
        return new Property<>(Point.ZERO);
    }

    public UICanvas addObject(UIObject object) {
        return addObject(object, null);
    }

    public UICanvas addObject(UIObject object, PositioningAlgorithm<Point> algorithm) {
        object.init(this);
        objects.add(object);
        objectsPositions.put(object, new Property<>(Point.ZERO));

        if (algorithm != null)
            objectsPositions.get(object).set(() -> algorithm.calculate(object));

        return this;
    }

    public Property<Point> getObjectPosition(UIObject object){
        return objectsPositions.get(object);
    }

    public boolean removeObject(String name) {
        for (int i = 0; i < objects.size(); i++) {
            UIObject object = objects.get(i);

            if (object.getName().equals(name)) {
                objectsPositions.remove(object);
                objects.remove(i);

                return true;
            }

        }
        return false;
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
    }

    @Override
    public void render() {
        ViewTools viewTools = GLContext.getCurrent().getViewTools();

        for (UIObject object : objects){
            Point objectPosition = objectsPositions.get(object).get();
            Size objectSize = object.size().get();

            viewTools.pushLook(new Rect(objectPosition.x, objectPosition.y, objectSize.width, objectSize.height));
            object.render();
            viewTools.popLook();
        }
    }

}