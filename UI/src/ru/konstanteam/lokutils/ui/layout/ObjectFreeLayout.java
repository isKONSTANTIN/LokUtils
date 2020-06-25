package ru.konstanteam.lokutils.ui.layout;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.ui.UIObject;

import java.util.ArrayList;

public abstract class ObjectFreeLayout extends UIAbstractLayout {
    public <T extends UIObject> T getObject(Class<T> objectType, String name) {
        for (UIObject object : objects) {
            if (object.getName().equals(name) && objectType.isInstance(object))
                return (T) object;
        }

        return null;
    }

    public <T extends UIObject> T getObject(Class<T> objectType) {
        for (UIObject object : objects) {
            if (objectType.isInstance(object))
                return (T) object;
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

    public ArrayList<UIObject> getObjects() {
        return objects;
    }

    public Point getObjectPosition(UIObject object) {
        return getObjectPos(object);
    }
}
