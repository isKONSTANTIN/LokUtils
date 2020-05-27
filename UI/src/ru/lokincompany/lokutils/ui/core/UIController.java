package ru.lokincompany.lokutils.ui.core;

import ru.lokincompany.lokutils.input.Mouse;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.events.ClickType;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;

public abstract class UIController {
    protected Window window;

    public UIController(Window window) {
        this.window = window;
    }

    protected Event checkEvent(){
        Mouse mouse = window.getInputs().mouse;
        Rect windowRect = new Rect(Point.ZERO, new Size(window.getResolution()));
        Event event = null;

        Point mousePosition = mouse.getMousePosition();

        if (windowRect.inside(mousePosition)) {
            boolean pressedStatus = mouse.getPressedStatus();
            boolean lastMousePressed = mouse.getLastMousePressed();

            if (pressedStatus && !lastMousePressed)
                event = new MouseClickedEvent(mousePosition, ClickType.CLICKED, mouse.buttonID);
            else if (!pressedStatus && lastMousePressed)
                event = new MouseClickedEvent(mousePosition, ClickType.REALIZED, mouse.buttonID);
        }

        return event;
    }

    public abstract void update();
    public abstract void render();
}
