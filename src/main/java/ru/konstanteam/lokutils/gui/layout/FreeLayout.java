package ru.konstanteam.lokutils.gui.layout;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.tools.property.PropertyBasic;

import java.util.HashMap;

public class FreeLayout extends ObjectFreeLayout {
    protected HashMap<GUIObject, PropertyBasic<Point>> positions = new HashMap<>();

    @Override
    protected Point getObjectPos(GUIObject object) {
        return positions.get(object).get();
    }

    public PropertyBasic<Point> getObjectPropertyPosition(GUIObject object) {
        return positions.get(object);
    }

    @Override
    public void calculateAll() {

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
        addObject(object, new PropertyBasic<>(point));
    }

    public void addObject(GUIObject object, PropertyBasic<Point> position) {
        this.addObject(object);

        positions.put(object, position);
    }

    public void addObject(GUIObject object, Alignment alignment) {
        PropertyBasic<Point> propertyBasic = new PropertyBasic<>(Point.ZERO);
        propertyBasic.track(() -> alignment.getPosition(object.size().get(), size().get()));

        this.addObject(object, propertyBasic);
    }
}
