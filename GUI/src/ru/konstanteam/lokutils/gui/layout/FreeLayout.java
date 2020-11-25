package ru.konstanteam.lokutils.gui.layout;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.tools.property.Property;

import java.util.HashMap;

public class FreeLayout extends ObjectFreeLayout {
    protected HashMap<GUIObject, Property<Point>> positions = new HashMap<>();

    @Override
    protected Point getObjectPos(GUIObject object) {
        return positions.get(object).get();
    }

    public Property<Point> getObjectPropertyPosition(GUIObject object) {
        return positions.get(object);
    }

    @Override
    protected void calculateAll() {
    }

    public boolean removeObject(GUIObject object) {
        boolean result = super.removeObject(object);
        if (result)
            positions.remove(object);
        return result;
    }

    public void removeAll() {
        super.removeAll();
        positions.clear();
    }

    public void addObject(GUIObject object, Point point) {
        addObject(object, new Property<>(point));
    }

    public void addObject(GUIObject object, Property<Point> position) {
        super.addObject(object);

        positions.put(object, position);
    }

    public void addObject(GUIObject object, Alignment alignment) {
        Property<Point> property = new Property<>(Point.ZERO);
        property.set(() -> alignment.getPosition(object.size().get(), size().get()));

        this.addObject(object, property);
    }
}
