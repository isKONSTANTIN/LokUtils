package ru.konstanteam.lokutils.ui.core.windows.bar;

import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.ui.core.windows.window.AbstractWindow;
import ru.konstanteam.lokutils.ui.eventsystem.CustomersContainer;

public abstract class AbstractWindowBar<T extends AbstractWindow> {
    public abstract void render();

    public abstract void init(T window);

    public abstract void update();

    public abstract Rect getRect();

    public abstract CustomersContainer getCustomersContainer();

    public abstract String getTitle();

    public abstract void setTitle(String text);
}
