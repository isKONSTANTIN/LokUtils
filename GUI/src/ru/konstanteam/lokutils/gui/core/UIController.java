package ru.konstanteam.lokutils.gui.core;

import org.lwjgl.util.vector.Vector2f;
import ru.konstanteam.lokutils.input.Mouse;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.gui.GUIStyle;
import ru.konstanteam.lokutils.gui.eventsystem.Event;
import ru.konstanteam.lokutils.gui.eventsystem.events.*;

public abstract class UIController {
    protected Window window;
    protected GUIStyle style;

    protected Event lastEvent;
    protected Point lastMousePosition = Point.ZERO;

    public GUIStyle getStyle() {
        return style;
    }

    public void setStyle(GUIStyle style) {
        this.style = style;
    }

    protected Event checkEvent() {
        Mouse mouse = window.getInputs().mouse;
        Event event = null;

        Point mousePosition = mouse.getMousePosition();
        Vector2f mouseScroll = mouse.getMouseScroll();

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
        } else if (window.getInputs().keyboard.nextKey()) {
            event = new KeyTypedEvent(window.getInputs().keyboard.getPressedKey());
        } else if (window.getInputs().keyboard.nextChar()) {
            event = new CharTypedEvent(window.getInputs().keyboard.getPressedChar());
        } else if (mouseScroll.x != 0 || mouseScroll.y != 0) {
            event = new MouseScrollEvent(new Point(mouseScroll.x, mouseScroll.y));
        }

        lastEvent = event;
        lastMousePosition = mousePosition;
        return event;
    }

    public abstract void update();

    public abstract void render();

    public void init(Window window, GUIStyle GUIStyle) {
        this.window = window;
        this.style = GUIStyle;
    }
}
