package su.knst.lokutils.gui.layout;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.objects.Point;

import java.util.ArrayList;

public abstract class ObjectFreeLayout extends GUIAbstractLayout {
    public <T extends GUIObject> T getObject(Class<T> objectType, String name) {
        for (GUIObject object : objects) {
            if (object.getName().equals(name) && objectType.isInstance(object))
                return (T) object;
        }

        return null;
    }

    public <T extends GUIObject> T getObject(Class<T> objectType) {
        for (GUIObject object : objects) {
            if (objectType.isInstance(object))
                return (T) object;
        }

        return null;
    }

    public GUIObject getObject(String name) {
        for (GUIObject object : objects) {
            if (object.getName().equals(name))
                return object;
        }

        return null;
    }

    public boolean removeObject(GUIObject object) {
        return super.removeObject(object);
    }

    public void removeAll() {
        super.removeAll();
    }

    public ArrayList<GUIObject> getObjects() {
        return objects;
    }

    public Point getObjectPosition(GUIObject object) {
        return getObjectPos(object);
    }
}
