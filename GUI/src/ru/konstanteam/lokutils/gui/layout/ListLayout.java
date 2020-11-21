package ru.konstanteam.lokutils.gui.layout;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.gui.GUIObject;

import java.util.HashMap;

public class ListLayout extends ObjectFreeLayout {
    protected HashMap<GUIObject, Point> positions = new HashMap<>();
    protected float gap = 2;

    public ListLayout() {

    }

    public void addObject(GUIObject object) {
        super.addObject(object);
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }

    @Override
    protected Point getObjectPos(GUIObject object) {
        return positions.get(object);
    }

    @Override
    protected Point getLazyObjectPos(GUIObject object) {
        return positions.get(object);
    }

    @Override
    protected void calculateAll() {
        float x = 0;
        float y = 0;

        for (GUIObject object : objects) {
            Size size = object.size().get();

            positions.put(object, new Point(0, y));

            x = Math.max(x, size.width);
            y += size.height + gap;
        }

        size.set(new Size(x, y));
    }
}
