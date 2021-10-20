package su.knst.lokutils.gui.layout;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Size;

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
    public void calculateAll() {
        if (isValid)
            return;

        float filledX = 0;
        float filledY = 0;
        float maxHeightLine = 0;
        float maxX = 0;

        float gap = this.gap;
        int i = 0;

        Size mySize = size.get();

        for (GUIObject object : objects) {
            if (i == objects.size() - 1)
                gap = 0;

            Size objectSize = object.size().get().offset(gap, gap);

            if (filledX + objectSize.width > mySize.width) {
                filledY += maxHeightLine;
                filledX = 0;
                maxHeightLine = 0;
            }

            positions.put(object, new Point(filledX, filledY));

            filledX += objectSize.width;

            maxX = Math.max(maxX, filledX);

            maxHeightLine = Math.max(maxHeightLine, objectSize.height);

            i++;
        }
        filledY += maxHeightLine;

        minimumSize().set(new Size(maxX, filledY));

        isValid = true;
    }

    @Override
    protected void setInvalidStatus() {
        isValid = false;
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
