package ru.konstanteam.lokutils.gui.eventsystem.events;

import ru.konstanteam.lokutils.gui.eventsystem.Event;
import ru.konstanteam.lokutils.objects.Point;

public class MouseScrollEvent extends Event {
    public final Point scrollDelta;

    public MouseScrollEvent(Point scrollDelta) {
        this.scrollDelta = scrollDelta;
    }
}
