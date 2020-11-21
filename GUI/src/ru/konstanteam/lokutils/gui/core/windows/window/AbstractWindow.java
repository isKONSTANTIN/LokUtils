package ru.konstanteam.lokutils.gui.core.windows.window;

import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.gui.GUIStyle;
import ru.konstanteam.lokutils.gui.core.windows.UIWindowSystem;
import ru.konstanteam.lokutils.gui.core.windows.bar.AbstractWindowBar;
import ru.konstanteam.lokutils.gui.eventsystem.Event;
import ru.konstanteam.lokutils.gui.layout.GUIAbstractLayout;

public abstract class AbstractWindow<T extends GUIAbstractLayout, R extends AbstractWindowBar> {
    protected Size size = new Size(150, 150);
    protected Size minSize = new Size(50, 50);

    protected T layout;
    protected R bar;

    protected UIWindowSystem windowSystem;
    protected GUIStyle style;

    protected boolean minimized;
    protected boolean contendInited;

    public boolean closable() {
        return false;
    }

    public boolean resizable() {
        return true;
    }

    public UIWindowSystem getWindowSystem() {
        return windowSystem;
    }

    public GUIStyle getStyle() {
        return style;
    }

    public T getLayout() {
        return layout;
    }

    public R getBar() {
        return bar;
    }

    public boolean isMinimized() {
        return minimized;
    }

    public Size getContentSize() {
        return isMinimized() ? layout.size().get().setHeight(0) : layout.size().get();
    }

    public Size getMinSize() {
        return minSize;
    }

    protected abstract T initLayout();

    protected abstract R initBar();

    public void initContent(UIWindowSystem windowSystem) {
        contendInited = true;

        if (style == null)
            style = windowSystem.getStyle();

        if (layout == null)
            layout = initLayout();

        if (bar == null)
            bar = initBar();

        this.layout.size().set(() -> size);
    }

    public void init(UIWindowSystem windowSystem) {
        this.windowSystem = windowSystem;

        if (!contendInited)
            initContent(windowSystem);
    }

    public abstract void handleEvent(Event event);

    public abstract void render();
}
