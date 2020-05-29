package ru.lokincompany.lokutils.ui.core;

import ru.lokincompany.lokutils.input.Mouse;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIStyle;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.events.ClickType;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;

public abstract class UIController {
    protected Window window;
    protected UIStyle style;

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

    protected Event checkEvent(){
        Mouse mouse = window.getInputs().mouse;
        Rect windowRect = new Rect(Point.ZERO, new Size(window.getResolution()));
        Event event = null;

        Point mousePosition = mouse.getMousePosition();

        boolean pressedStatus = mouse.getPressedStatus();
        boolean lastMousePressed = mouse.getLastMousePressed();

        if (windowRect.inside(mousePosition)) {
            if (pressedStatus && !lastMousePressed)
                event = new MouseClickedEvent(mousePosition, ClickType.CLICKED, mouse.buttonID);
            else if (!pressedStatus && lastMousePressed)
                event = new MouseClickedEvent(mousePosition, ClickType.REALIZED, mouse.buttonID);
        }else if (!pressedStatus && lastMousePressed)
            event = new MouseClickedEvent(mousePosition, ClickType.UNCLICKED, mouse.buttonID);

        return event;
    }

    public abstract void update();
    public abstract void render();
}
