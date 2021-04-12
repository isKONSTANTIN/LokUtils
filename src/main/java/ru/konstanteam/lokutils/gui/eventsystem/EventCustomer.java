package ru.konstanteam.lokutils.gui.eventsystem;

import ru.konstanteam.lokutils.gui.eventsystem.events.Event;

public interface EventCustomer<T extends Event> {
    void handle(T event);
}
