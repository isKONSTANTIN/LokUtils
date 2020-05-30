package ru.lokincompany.lokutils.ui.core.windows;

import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.tools.Removable;
import ru.lokincompany.lokutils.ui.core.UIController;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.events.ClickType;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseMoveEvent;
import ru.lokincompany.lokutils.ui.eventsystem.events.MoveType;

import java.util.ArrayList;

public class UIWindowSystem extends UIController {
    protected ArrayList<UIWindow> windows = new ArrayList<>();

    public UIWindowSystem(Window window) {
        super(window);
    }

    public Removable addWindow(UIWindow window){
        window.init(this);

        windows.add(window);
        return () -> windows.remove(window);
    }

    public void closeWindow(UIWindow window){
        windows.remove(window);
    }

    @Override
    public void update() {
        Event event = checkEvent();
        if (event == null) return;
        UIWindow targetWindow = null;

        if (event instanceof MouseClickedEvent){
            MouseClickedEvent mouseClickedEvent = (MouseClickedEvent) event;

            for (UIWindow window : windows) {
                Rect barField = new Rect(window.position, window.contentSize.setHeight(window.barSize));
                Rect contentField = new Rect(window.position.offset(0, barField.getHeight()), window.contentSize);

                if (mouseClickedEvent.clickType == ClickType.UNCLICKED){
                    window.handleBarEvent(new MouseClickedEvent(mouseClickedEvent.position.relativeTo(barField.position), mouseClickedEvent.clickType, mouseClickedEvent.button));
                    window.handleContentEvent(new MouseClickedEvent(mouseClickedEvent.position.relativeTo(contentField.position), mouseClickedEvent.clickType, mouseClickedEvent.button));
                    continue;
                }

                if (barField.inside(mouseClickedEvent.position)) {
                    window.handleBarEvent(new MouseClickedEvent(mouseClickedEvent.position.relativeTo(barField.position), mouseClickedEvent.clickType, mouseClickedEvent.button));
                    targetWindow = window;
                    break;
                } else if (contentField.inside(mouseClickedEvent.position)) {
                    window.handleContentEvent(new MouseClickedEvent(mouseClickedEvent.position.relativeTo(contentField.position), mouseClickedEvent.clickType, mouseClickedEvent.button));
                    targetWindow = window;
                    break;
                }
            }
        }else if (event instanceof MouseMoveEvent) {
            MouseMoveEvent mouseMoveEvent = (MouseMoveEvent) event;

            for (UIWindow window : windows) {
                Rect barField = new Rect(window.position, window.contentSize.setHeight(window.barSize));
                Rect contentField = new Rect(window.position.offset(0, barField.getHeight()), window.contentSize);

                if (barField.inside(mouseMoveEvent.lastPosition)){
                    window.handleBarEvent(new MouseMoveEvent(mouseMoveEvent.startPosition.relativeTo(barField.position), mouseMoveEvent.lastPosition.relativeTo(barField.position), mouseMoveEvent.endPosition.relativeTo(barField.position), mouseMoveEvent.type));
                    if (mouseMoveEvent.type == MoveType.STARTED)
                        targetWindow = window;
                    break;
                }else if (contentField.inside(mouseMoveEvent.lastPosition)) {
                    window.handleContentEvent(new MouseMoveEvent(mouseMoveEvent.startPosition.relativeTo(contentField.position), mouseMoveEvent.lastPosition.relativeTo(contentField.position), mouseMoveEvent.endPosition.relativeTo(contentField.position), mouseMoveEvent.type));
                    if (mouseMoveEvent.type == MoveType.STARTED)
                        targetWindow = window;
                    break;
                }
            }
        }

        if (targetWindow == null) return;

        if (windows.contains(targetWindow)){
            windows.remove(targetWindow);
            windows.add(0, targetWindow);
        }
    }

    @Override
    public void render() {
        ViewTools viewTools = GLContext.getCurrent().getViewTools();

        for (int i = windows.size() - 1; i >= 0; i--){
            UIWindow window = windows.get(i);
            Rect barField = new Rect(window.position, window.contentSize.setHeight(window.barSize));
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
