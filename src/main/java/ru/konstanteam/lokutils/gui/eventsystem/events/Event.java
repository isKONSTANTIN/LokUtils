package ru.konstanteam.lokutils.gui.eventsystem.events;

import ru.konstanteam.lokutils.objects.Point;

public class Event {
    protected boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public Event relativeTo(Point position) {
        return this;
    }
}
