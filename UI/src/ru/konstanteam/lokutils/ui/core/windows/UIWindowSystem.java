package ru.konstanteam.lokutils.ui.core.windows;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.render.tools.ViewTools;
import ru.konstanteam.lokutils.tools.Removable;
import ru.konstanteam.lokutils.ui.core.UIController;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.eventsystem.events.ClickType;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.ui.eventsystem.events.MoveType;

import java.util.ArrayList;
import java.util.HashMap;

public class UIWindowSystem extends UIController {
    protected ArrayList<UIWindow> windows = new ArrayList<>();
    protected ArrayList<WindowTask> windowTasks = new ArrayList<>();
    protected HashMap<UIWindow, Boolean> resizeStatus = new HashMap<>();

    public UIWindowSystem(Window window) {
        super(window);
    }

    public Removable addWindow(UIWindow window) {
        window.init(this);

        windows.add(window);
        return () -> closeWindow(window);
    }

    public void closeWindow(UIWindow window) {
        windowTasks.add(() -> windows.remove(window));
    }

    public void bringToFront(UIWindow window) {
        windowTasks.add(() -> {
            if (!windows.contains(window)) return;

            windows.remove(window);
            windows.add(0, window);
        });
    }

    protected boolean handleMouseClickedEvent(MouseClickedEvent event, UIWindow window) {
        Rect barField = window.getBar().getRect().offset(window.position);
        Rect contentField = new Rect(window.position.offset(0, barField.getHeight()), window.contentSize);

        if (event.clickType == ClickType.UNCLICKED) {
            window.handleBarEvent(event.relativeTo(barField.position));
            window.handleContentEvent(event.relativeTo(contentField.position));
            return false;
        }

        boolean insideBar = barField.inside(event.position);
        boolean insideContent = contentField.inside(event.position);

        if (!insideBar && !insideContent)
            return false;

        if (insideBar)
            window.handleBarEvent(event.relativeTo(barField.position));
        else
            window.handleContentEvent(event.relativeTo(contentField.position));

        bringToFront(window);
        return true;
    }

    protected boolean handleMouseMoveEvent(MouseMoveEvent event, UIWindow window) {
        Rect barField = window.getBar().getRect().offset(window.position);
        Rect contentField = new Rect(window.position.offset(0, barField.getHeight()), window.contentSize);

        boolean insideBar = barField.inside(event.lastPosition);
        boolean insideContent = contentField.inside(event.lastPosition);

        if (contentField.getBottomRightPoint().distance(event.startPosition) < 3 && event.type == MoveType.STARTED || event.type == MoveType.CONTINUED && resizeStatus.getOrDefault(window, false)) {
            if (event.type == MoveType.STARTED)
                bringToFront(window);

            Point deltaPos = event.endPosition.relativeTo(event.lastPosition);
            Size newSize = window.contentSize.offset(deltaPos.x, deltaPos.y);

            window.contentSize = new Size(
                    Math.max(newSize.width, window.minContentSize.width),
                    Math.max(newSize.height, window.minContentSize.height)
            );
            resizeStatus.put(window, true);
            return true;
        }

        resizeStatus.put(window, false);

        if (!insideBar && !insideContent)
            return false;

        if (insideBar)
            window.handleBarEvent(event.relativeTo(barField.position));
        else
            window.handleContentEvent(event.relativeTo(contentField.position));

        if (event.type == MoveType.STARTED)
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

        for (UIWindow window : windows) {
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
            UIWindow window = windows.get(i);
            Rect barField = window.getBar().getRect().offset(window.position);
            Rect contentField = new Rect(window.position.offset(0, barField.getHeight()), window.contentSize);

            viewTools.pushLook(barField);
            window.renderBar();
            viewTools.popLook();

            viewTools.pushLook(contentField);
            window.renderContent();
            viewTools.popLook();
        }
    }
}
