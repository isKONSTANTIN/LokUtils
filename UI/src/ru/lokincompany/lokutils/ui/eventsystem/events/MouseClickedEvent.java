package ru.lokincompany.lokutils.ui.eventsystem.events;

import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.ui.eventsystem.Event;

public class MouseClickedEvent extends Event {
    public final Point position;
    public final int button;
    public final ClickType clickType;

    public MouseClickedEvent(Point position, ClickType clickType, int button) {
        this.position = position;
        this.button = button;
        this.clickType = clickType;
    }
}

