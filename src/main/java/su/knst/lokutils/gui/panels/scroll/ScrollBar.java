package su.knst.lokutils.gui.panels.scroll;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.tools.property.PropertyBasic;

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
