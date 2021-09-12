package ru.konstanteam.lokutils.gui.panels.scroll;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.tools.property.PropertyBasic;

public abstract class ScrollBar extends GUIObject {
    public static int BAR_SIZE = 12;

    protected ScrollPanel panel;

    protected PropertyBasic<Point> headPosition = new PropertyBasic<>(Point.ZERO);
    protected PropertyBasic<Size> headSize = new PropertyBasic<>(Size.ZERO);
    protected Rect startMoveBarState = Rect.ZERO;

    public abstract boolean active();

    public ScrollBar(ScrollPanel panel){
        this.panel = panel;
    }
}
