package ru.konstanteam.lokutils.gui.eventsystem;

import ru.konstanteam.lokutils.gui.eventsystem.events.Event;

public interface EventHandler<T extends Event> {
    T handle(T event);
}
