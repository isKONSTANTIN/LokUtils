package su.knst.lokutils.gui.core.windows.bar;

import su.knst.lokutils.gui.core.windows.window.GUIWindow;
import su.knst.lokutils.gui.eventsystem.CustomersContainer;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.tools.property.PropertyBasic;

public abstract class GUIWindowBar<T extends GUIWindow> {
    protected final PropertyBasic<Size> minimumSize = new PropertyBasic<>(Size.ZERO);

    public abstract void render();

    public abstract void init(T window);

    public abstract void update();

    public abstract Rect getRect();

    public PropertyBasic<Size> minimumSize() {
        return minimumSize;
    }

    public abstract CustomersContainer getCustomersContainer();

    public abstract String getTitle();

    public abstract void setTitle(String text);
}
