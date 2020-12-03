package ru.konstanteam.lokutils.gui.core.windows.window;

import ru.konstanteam.lokutils.gui.GUIStyle;
import ru.konstanteam.lokutils.gui.core.windows.GUIWindowSystem;
import ru.konstanteam.lokutils.gui.eventsystem.Event;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.tools.property.Property;

public abstract class GUIWindow {
    protected final Property<Size> size = new Property<>(new Size(150, 150));
    protected final Property<Size> minimumSize = new Property<>(Size.ZERO);
    protected final Property<Size> maximumSize = new Property<>(new Size(Float.MAX_VALUE, Float.MAX_VALUE));
    protected final Property<Boolean> focused = new Property<>(false);
    protected final Property<Point> contentOffset = new Property<>(Point.ZERO);

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

    public abstract Property<Size> contentSize();

    public Property<Point> contentOffset() {
        return contentOffset;
    }

    public Property<Size> minimumSize() {
        return minimumSize;
    }

    public Property<Size> size() {
        return size;
    }

    public Property<Size> maximumSize() {
        return maximumSize;
    }

    public Property<Boolean> focused() {
        return focused;
    }
}
