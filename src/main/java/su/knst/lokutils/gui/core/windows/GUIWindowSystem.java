package su.knst.lokutils.gui.core.windows;

import su.knst.lokutils.gui.core.GUIController;
import su.knst.lokutils.gui.core.windows.window.GUIWindow;
import su.knst.lokutils.gui.eventsystem.events.Event;
import su.knst.lokutils.gui.eventsystem.events.ClickType;
import su.knst.lokutils.gui.eventsystem.events.MouseClickedEvent;
import su.knst.lokutils.gui.eventsystem.events.MouseMoveEvent;
import su.knst.lokutils.gui.eventsystem.events.MoveType;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.render.tools.ViewTools;
import su.knst.lokutils.tools.Removable;

import java.util.ArrayList;
import java.util.HashMap;

public class GUIWindowSystem extends GUIController {
    protected ArrayList<GUIWindow> windows = new ArrayList<>();
    protected ArrayList<WindowTask> windowTasks = new ArrayList<>();
    protected ArrayList<WindowTask> windowCreationTasks = new ArrayList<>();
    protected HashMap<GUIWindow, Point> windowsPositions = new HashMap<>();

    public Removable addWindow(GUIWindow window) {
        WindowTask task = () -> {
            window.init(this);

            windows.add(window);
            windowsPositions.put(window,
                    new Point(
                            this.window.getResolution().getX() / 2f - window.contentSize().get().width / 2f,
                            this.window.getResolution().getY() / 2f - window.contentSize().get().height / 2f
                    )
            );
        };

        windowCreationTasks.add(task);

        return () -> closeWindow(window);
    }

    public void closeWindow(GUIWindow window) {
        windowTasks.add(() -> windows.remove(window));
    }

    public Point getWindowsPosition(GUIWindow window) {
        return windowsPositions.get(window);
    }

    public void setWindowsPosition(Point position, GUIWindow window) {
        windowTasks.add(() -> {
            windowsPositions.put(window, position);
        });
    }

    public void bringToFront(GUIWindow window) {
        windowTasks.add(() -> {
            if (!windows.contains(window)) return;

            windows.remove(window);
            windows.add(0, window);
        });
    }

    protected boolean handleMouseClickedEvent(MouseClickedEvent event, GUIWindow window) {
        Point windowPosition = windowsPositions.getOrDefault(window, Point.ZERO);
        Rect field = new Rect(windowPosition, window.size().get());

        boolean inside = field.inside(event.position);

        if (inside || event.clickType == ClickType.UNCLICKED)
            window.handleEvent(event.relativeTo(windowPosition));

        if (!inside)
            return false;

        if (event.clickType == ClickType.CLICKED)
            bringToFront(window);

        return event.clickType == ClickType.CLICKED;
    }

    protected boolean handleMouseMoveEvent(MouseMoveEvent event, GUIWindow window) {
        Point windowPosition = windowsPositions.getOrDefault(window, Point.ZERO);
        Rect field = new Rect(windowPosition, window.size().get());

        boolean inside = field.inside(event.lastPosition);

        window.handleEvent(event.relativeTo(windowPosition));

        if (event.type == MoveType.STARTED && inside) {
            bringToFront(window);
        }

        return true;
    }

    @Override
    public void update() {
        for (WindowTask task : windowTasks)
            task.run();

        windowTasks.clear();

        Event event = checkEvent();
        if (event == null) return;

        boolean stopCycle = true;
        for (GUIWindow window : windows) {
            if (event instanceof MouseClickedEvent)
                stopCycle = handleMouseClickedEvent((MouseClickedEvent) event, window);
            else if (event instanceof MouseMoveEvent)
                stopCycle = handleMouseMoveEvent((MouseMoveEvent) event, window);
            else
                window.handleEvent(event);

            if (stopCycle)
                break;
        }
    }

    @Override
    public void render() {
        for (WindowTask task : windowCreationTasks)
            task.run();

        windowCreationTasks.clear();

        ViewTools viewTools = GLContext.getCurrent().getViewTools();

        for (int i = windows.size() - 1; i >= 0; i--) {
            GUIWindow window = windows.get(i);
            Point windowPosition = windowsPositions.getOrDefault(window, Point.ZERO);

            viewTools.pushTranslate(windowPosition);
            window.render();
            viewTools.popTranslate();
        }
    }
}
