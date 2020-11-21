package ru.konstanteam.lokutils.gui.eventsystem.events;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.gui.eventsystem.Event;

public class MouseScrollEvent extends Event {
    public final Point scrollDelta;

    public MouseScrollEvent(Point scrollDelta) {
        this.scrollDelta = scrollDelta;
    }
}
