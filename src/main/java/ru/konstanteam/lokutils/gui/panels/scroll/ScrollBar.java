package ru.konstanteam.lokutils.gui.panels.scroll;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.tools.property.Property;

public abstract class ScrollBar extends GUIObject {
    public static int BAR_SIZE = 12;

    protected ScrollPanel panel;

    protected Property<Point> headPosition = new Property<>(Point.ZERO);
    protected Property<Size> headSize = new Property<>(Size.ZERO);
    protected Rect startMoveBarState = Rect.ZERO;

    public abstract boolean active();

    public ScrollBar(ScrollPanel panel){
        this.panel = panel;
    }
}
