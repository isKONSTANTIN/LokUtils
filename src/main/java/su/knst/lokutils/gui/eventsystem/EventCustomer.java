package su.knst.lokutils.gui.eventsystem;

import su.knst.lokutils.gui.eventsystem.events.Event;

public interface EventCustomer<T extends Event> {
    void handle(T event);
}
