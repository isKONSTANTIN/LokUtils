package ru.konstanteam.lokutils.gui.layout;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;

import java.util.HashMap;

public class ListLayout extends ObjectFreeLayout {
    protected HashMap<GUIObject, Point> positions = new HashMap<>();
    protected HashMap<GUIObject, Alignment> alignments = new HashMap<>();

    protected float gap = 2;

    public ListLayout() {

    }

    public void addObject(GUIObject object, Alignment alignment) {
        super.addObject(object);

        alignments.put(object, alignment);
    }

    public void addObject(GUIObject object) {
        addObject(object, Alignment.TOP_LEFT);
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }

    @Override
    public boolean removeObject(GUIObject object) {
        boolean removed = super.removeObject(object);

        if (removed){
            alignments.remove(object);
            positions.remove(object);
        }

        return removed;
    }

    @Override
    public void removeAll() {
        super.removeAll();

        alignments.clear();
        positions.clear();
    }

    @Override
    protected Point getObjectPos(GUIObject object) {
        return positions.get(object);
    }

    @Override
    protected void calculateAll() {
        float x = 0;
        float y = 0;

        Size mySize = size().get();

        for (GUIObject object : objects) {
            Size size = object.size().get();

            Point alignmentPos = alignments.get(object).getPosition(size, mySize);

            positions.put(object, new Point(alignmentPos.x, y));

            x = Math.max(x, size.width);
            y += size.height + gap;
        }

        minimumSize().set(new Size(x, y));
    }
}
