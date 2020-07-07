package ru.konstanteam.lokutils.ui.core.windows.window;

import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.ui.UIStyle;
import ru.konstanteam.lokutils.ui.core.windows.UIWindowSystem;
import ru.konstanteam.lokutils.ui.core.windows.bar.AbstractWindowBar;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.layout.UIAbstractLayout;

public abstract class AbstractWindow<T extends UIAbstractLayout, R extends AbstractWindowBar> {
    protected Size size = new Size(150, 150);
    protected Size minSize = new Size(50, 50);

    protected T layout;
    protected R bar;

    protected UIWindowSystem windowSystem;
    protected UIStyle style;

    protected boolean minimized;

    public AbstractWindow(T rootLayout, R bar) {
        this.layout = rootLayout;
        this.bar = bar;

        this.layout.size().set(() -> size);
    }

    public boolean closable() {
        return false;
    }

    public boolean resizable() {
        return false;
    }

    public UIWindowSystem getWindowSystem() {
        return windowSystem;
    }

    public UIStyle getStyle() {
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

    public void init(UIWindowSystem windowSystem) {
        this.windowSystem = windowSystem;

        if (this.style == null)
            this.style = windowSystem.getStyle();
    }

    public abstract void handleEvent(Event event);

    public abstract void render();
}
