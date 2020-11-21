package ru.konstanteam.lokutils.gui.layout;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.gui.GUIObject;

import java.util.HashMap;

public class BaseLayout extends ObjectFreeLayout {
    protected HashMap<GUIObject, Point> positions = new HashMap<>();
    protected float gap;

    public BaseLayout(float gap) {
        this.gap = gap;
    }

    public BaseLayout() {
        this(5);
    }

    @Override
    protected Point getObjectPos(GUIObject object) {
        return positions.get(object);
    }

    @Override
    protected Point getLazyObjectPos(GUIObject object) {
        return getObjectPos(object);
    }

    @Override
    protected void calculateAll() {
        float filledX = 0;
        float filledY = 0;
        float maxHeightLine = 0;

        Size mySize = size.get();

        for (GUIObject object : objects) {
            Size objectSize = object.size().get().offset(gap, gap);

            if (filledX + objectSize.width > mySize.width) {
                filledY += maxHeightLine;
                filledX = 0;
                maxHeightLine = 0;
            }

            positions.put(object, new Point(filledX, filledY));

            filledX += objectSize.width;
            maxHeightLine = Math.max(maxHeightLine, objectSize.height);
        }

        this.isValid = true;
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
        setInvalidStatus();
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

    public void addObject(GUIObject object) {
        super.addObject(object);
        setInvalidStatus();
    }

}
