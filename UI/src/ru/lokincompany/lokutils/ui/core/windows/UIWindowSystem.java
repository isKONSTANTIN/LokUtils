package ru.lokincompany.lokutils.ui.core.windows;

import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.tools.Removable;
import ru.lokincompany.lokutils.ui.UIStyle;
import ru.lokincompany.lokutils.ui.core.UIController;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.lokincompany.lokutils.ui.objects.UICanvas;

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

        if (event instanceof MouseClickedEvent){
            MouseClickedEvent mouseClickedEvent = (MouseClickedEvent) event;
            UIWindow targetWindow = null;

            for (UIWindow window : windows) {
                Rect barField = new Rect(window.position, window.contentSize.setHeight(window.barSize));
                Rect contentField = new Rect(window.position.offset(0, barField.getHeight()), window.contentSize);

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

            if (targetWindow == null) return;

            if (windows.contains(targetWindow)){
                windows.remove(targetWindow);
                windows.add(0, targetWindow);
            }
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
