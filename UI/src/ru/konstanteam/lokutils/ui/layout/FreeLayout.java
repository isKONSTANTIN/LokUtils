package ru.konstanteam.lokutils.ui.layout;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.tools.property.Property;
import ru.konstanteam.lokutils.ui.UIObject;

import java.util.HashMap;

public class FreeLayout extends ObjectFreeLayout {
    protected HashMap<UIObject, Property<Point>> positions = new HashMap<>();

    @Override
    protected Point getObjectPos(UIObject object) {
        return positions.get(object).get();
    }

    @Override
    protected Point getLazyObjectPos(UIObject object) {
        return positions.get(object).lazyGet();
    }

    public Property<Point> getObjectPropertyPosition(UIObject object) {
        return positions.get(object);
    }

    @Override
    protected void calculateAll() {

    }

    public boolean removeObject(UIObject object) {
        boolean result = super.removeObject(object);
        if (result)
            positions.remove(object);
        return result;
    }

    public void removeAll() {
        super.removeAll();
        positions.clear();
    }

    public void addObject(UIObject object, Point point) {
        addObject(object, new Property<>(point));
    }

    public void addObject(UIObject object, Property<Point> position) {
        super.addObject(object);

        positions.put(object, position);
    }

    public void addObject(UIObject object, Alignment alignment) {
        Property<Point> property = new Property<>();
        property.set(() -> alignment.getPosition(object.size().get(), size().get()));

        this.addObject(object, property);
    }
}
