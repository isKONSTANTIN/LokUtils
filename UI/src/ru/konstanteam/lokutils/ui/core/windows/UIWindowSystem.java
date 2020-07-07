package ru.konstanteam.lokutils.ui.core.windows;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.render.tools.ViewTools;
import ru.konstanteam.lokutils.tools.Removable;
import ru.konstanteam.lokutils.ui.core.UIController;
import ru.konstanteam.lokutils.ui.core.windows.window.AbstractWindow;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.eventsystem.events.ClickType;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.ui.eventsystem.events.MoveType;

import java.util.ArrayList;
import java.util.HashMap;

public class UIWindowSystem extends UIController {
    protected ArrayList<AbstractWindow> windows = new ArrayList<>();
    protected ArrayList<WindowTask> windowTasks = new ArrayList<>();
    protected HashMap<AbstractWindow, Point> windowsPositions = new HashMap<>();

    public UIWindowSystem(Window window) {
        super(window);
    }

    public Removable addWindow(AbstractWindow window) {
        window.init(this);

        windows.add(window);
        windowsPositions.put(window,
                new Point(
                        this.window.getResolution().getX() / 2f - window.getContentSize().width / 2f,
                        this.window.getResolution().getY() / 2f - window.getContentSize().height / 2f
                )
        );
        return () -> closeWindow(window);
    }

    public void closeWindow(AbstractWindow window) {
        windowTasks.add(() -> windows.remove(window));
    }

    public Point getWindowsPosition(AbstractWindow window) {
        return windowsPositions.get(window);
    }

    public void setWindowsPosition(Point position, AbstractWindow window) {
        windowTasks.add(() -> {
            windowsPositions.put(window, position);
        });
    }

    public void bringToFront(AbstractWindow window) {
        windowTasks.add(() -> {
            if (!windows.contains(window)) return;

            windows.remove(window);
            windows.add(0, window);
        });
    }

    protected boolean handleMouseClickedEvent(MouseClickedEvent event, AbstractWindow window) {
        Point windowPosition = windowsPositions.getOrDefault(window, Point.ZERO);
        Rect field = new Rect(windowPosition, window.getContentSize()).merge(window.getBar().getRect().offset(windowPosition));

        boolean inside = field.inside(event.position);

        window.handleEvent(event.relativeTo(windowPosition));

        if (!inside || event.clickType == ClickType.UNCLICKED)
            return false;

        bringToFront(window);
        return true;
    }

    protected boolean handleMouseMoveEvent(MouseMoveEvent event, AbstractWindow window) {
        Point windowPosition = windowsPositions.getOrDefault(window, Point.ZERO);
        Rect field = new Rect(windowPosition, window.getContentSize()).merge(window.getBar().getRect().offset(windowPosition));

        boolean inside = field.inside(event.lastPosition);

        window.handleEvent(event.relativeTo(windowPosition));

        if (event.type == MoveType.STARTED && inside)
            bringToFront(window);

        return true;
    }

    @Override
    public void update() {
        for (WindowTask task : windowTasks)
            task.run();

        windowTasks.clear();

        Event event = checkEvent();
        if (event == null) return;

        for (AbstractWindow window : windows) {
            if (event instanceof MouseClickedEvent &&
                    handleMouseClickedEvent((MouseClickedEvent) event, window)) break;
            if (event instanceof MouseMoveEvent &&
                    handleMouseMoveEvent((MouseMoveEvent) event, window)) break;
        }
    }

    @Override
    public void render() {
        ViewTools viewTools = GLContext.getCurrent().getViewTools();

        for (int i = windows.size() - 1; i >= 0; i--) {
            AbstractWindow window = windows.get(i);
            Point windowPosition = windowsPositions.getOrDefault(window, Point.ZERO);

            viewTools.pushTranslate(windowPosition);
            window.render();
            viewTools.popTranslate();
        }
    }
}
