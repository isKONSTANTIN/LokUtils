package su.knst.lokutils.gui.eventsystem;

import su.knst.lokutils.gui.eventsystem.events.Event;

public interface EventHandler<T extends Event> {
    T handle(T event);
}
