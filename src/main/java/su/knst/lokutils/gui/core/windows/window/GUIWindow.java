package su.knst.lokutils.gui.core.windows.window;

import su.knst.lokutils.gui.style.GUIStyle;
import su.knst.lokutils.gui.core.windows.GUIWindowSystem;
import su.knst.lokutils.gui.eventsystem.events.Event;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.tools.property.PropertyBasic;

public abstract class GUIWindow {
    protected final PropertyBasic<Size> size = new PropertyBasic<>(new Size(150, 150));
    protected final PropertyBasic<Size> minimumSize = new PropertyBasic<>(Size.ZERO);
    protected final PropertyBasic<Size> maximumSize = new PropertyBasic<>(new Size(Float.MAX_VALUE, Float.MAX_VALUE));
    protected final PropertyBasic<Boolean> focused = new PropertyBasic<>(false);
    protected final PropertyBasic<Point> contentOffset = new PropertyBasic<>(Point.ZERO);

    protected GUIWindowSystem windowSystem;

    public abstract void handleEvent(Event event);

    public void init(GUIWindowSystem windowSystem) {
        this.windowSystem = windowSystem;
    }

    public GUIWindowSystem getWindowSystem() {
        return windowSystem;
    }

    public abstract GUIStyle getStyle();

    public abstract void render();

    public abstract PropertyBasic<Size> contentSize();

    public PropertyBasic<Point> contentOffset() {
        return contentOffset;
    }

    public PropertyBasic<Size> minimumSize() {
        return minimumSize;
    }

    public PropertyBasic<Size> size() {
        return size;
    }

    public PropertyBasic<Size> maximumSize() {
        return maximumSize;
    }

    public PropertyBasic<Boolean> focused() {
        return focused;
    }
}
