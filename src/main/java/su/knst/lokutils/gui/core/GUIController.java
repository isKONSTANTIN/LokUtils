package su.knst.lokutils.gui.core;

import org.lwjgl.util.vector.Vector2f;
import su.knst.lokutils.gui.eventsystem.events.Event;
import su.knst.lokutils.gui.eventsystem.events.*;
import su.knst.lokutils.input.Mouse;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.render.Window;
import su.knst.lokutils.gui.eventsystem.events.*;

public abstract class GUIController {
    protected Window window;

    protected Event lastEvent;
    protected Point lastMousePosition = Point.ZERO;

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
            if (lastEvent instanceof MouseMoveEvent)
                event = new MouseMoveEvent(((MouseMoveEvent) lastEvent).startPosition, lastMousePosition, mousePosition, MoveType.CONTINUED);
            else
                event = new MouseMoveEvent(((MouseClickedEvent) lastEvent).position, lastMousePosition, mousePosition, MoveType.STARTED);
        } else if (window.getInputs().keyboard.nextKey())
            event = new KeyTypedEvent(window.getInputs().keyboard.getPressedKey());
        else if (window.getInputs().keyboard.nextChar())
            event = new CharTypedEvent(window.getInputs().keyboard.getPressedChar());
        else if (mouseScroll.x != 0 || mouseScroll.y != 0)
            event = new MouseScrollEvent(new Point(mouseScroll.x, mouseScroll.y));

        lastEvent = event;
        lastMousePosition = mousePosition;

        return event;
    }

    public abstract void update();

    public abstract void render();

    public void init(Window window) {
        this.window = window;
    }
}
