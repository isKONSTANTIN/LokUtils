package ru.konstanteam.lokutils.ui.layout;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.ui.UIObject;

import java.util.HashMap;

public class BaseLayout extends ObjectFreeLayout {
    protected HashMap<UIObject, Point> positions = new HashMap<>();
    protected float gap;

    public BaseLayout(float gap) {
        this.gap = gap;
    }

    public BaseLayout() {
        this(5);
    }

    @Override
    protected Point getObjectPos(UIObject object) {
        return positions.get(object);
    }

    @Override
    protected Point getLazyObjectPos(UIObject object) {
        return getObjectPos(object);
    }

    @Override
    protected void calculateAll() {
        float filledX = 0;
        float filledY = 0;
        float maxHeightLine = 0;

        Size mySize = size.get();

        for (UIObject object : objects) {
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

        this.positionsIsValid = true;
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
        setInvalidPositionsStatus();
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

    public void addObject(UIObject object) {
        super.addObject(object);
        setInvalidPositionsStatus();
    }

}
