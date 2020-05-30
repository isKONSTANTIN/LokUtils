package ru.lokincompany.lokutils.ui.core;

import ru.lokincompany.lokutils.input.Mouse;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.ui.UIStyle;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.events.ClickType;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseMoveEvent;
import ru.lokincompany.lokutils.ui.eventsystem.events.MoveType;

public abstract class UIController {
    protected Window window;
    protected UIStyle style;

    protected Event lastEvent;
    protected Point lastMousePosition = Point.ZERO;

    public UIController(Window window, UIStyle style) {
        this.window = window;
        this.style = style;
    }

    public UIController(Window window) {
        this(window, UIStyle.getDefault());
    }

    public void setStyle(UIStyle style) {
        this.style = style;
    }

    public UIStyle getStyle() {
        return style;
    }

    protected Event checkEvent() {
        Mouse mouse = window.getInputs().mouse;
        Event event = null;

        Point mousePosition = mouse.getMousePosition();

        boolean pressedStatus = mouse.getPressedStatus();
        boolean lastMousePressed = mouse.getLastMousePressed();

        if (pressedStatus && !lastMousePressed)
            event = new MouseClickedEvent(mousePosition, ClickType.CLICKED, mouse.buttonID);
        else if (!pressedStatus && lastMousePressed)
            event = new MouseClickedEvent(mousePosition, ClickType.UNCLICKED, mouse.buttonID);
        else if (pressedStatus && lastMousePressed) {
            if (lastEvent instanceof MouseClickedEvent)
                event = new MouseMoveEvent(((MouseClickedEvent) lastEvent).position, lastMousePosition, mousePosition, MoveType.STARTED);
            else if (lastEvent instanceof MouseMoveEvent)
                event = new MouseMoveEvent(((MouseMoveEvent) lastEvent).startPosition, lastMousePosition, mousePosition, MoveType.CONTINUED);
        }

        lastEvent = event;
        lastMousePosition = mousePosition;
        return event;
    }

    public abstract void update();
    public abstract void render();
}
