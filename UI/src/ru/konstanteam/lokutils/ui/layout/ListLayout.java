package ru.konstanteam.lokutils.ui.layout;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.ui.UIObject;

import java.util.HashMap;

public class ListLayout extends ObjectFreeLayout {
    protected HashMap<UIObject, Point> positions = new HashMap<>();
    protected float gap = 2;

    public ListLayout() {

    }

    public void addObject(UIObject object) {
        super.addObject(object);
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }

    @Override
    protected Point getObjectPos(UIObject object) {
        return positions.get(object);
    }

    @Override
    protected Point getLazyObjectPos(UIObject object) {
        return positions.get(object);
    }

    @Override
    protected void calculateAll() {
        float x = 0;
        float y = 0;

        for (UIObject object : objects) {
            Size size = object.size().get();

            positions.put(object, new Point(0, y));

            x = Math.max(x, size.width);
            y += size.height + gap;
        }

        size.set(new Size(x, y));
    }
}
