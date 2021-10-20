package su.knst.lokutils.gui.eventsystem.events;

import su.knst.lokutils.objects.Point;

public class Event {
    protected boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public Event relativeTo(Point position) {
        return this;
    }
}
