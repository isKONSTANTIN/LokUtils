package ru.konstanteam.lokutils.ui.core;

import ru.konstanteam.lokutils.input.Mouse;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.ui.UIStyle;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.eventsystem.events.*;

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

    public UIStyle getStyle() {
        return style;
    }

    public void setStyle(UIStyle style) {
        this.style = style;
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
        }else if (window.getInputs().keyboard.nextKey()){
            event = new KeyTypedEvent(window.getInputs().keyboard.getPressedKey());
        }else if (window.getInputs().keyboard.nextChar()){
            event = new CharTypedEvent(window.getInputs().keyboard.getPressedChar());
        }

        lastEvent = event;
        lastMousePosition = mousePosition;
        return event;
    }

    public abstract void update();

    public abstract void render();
}
