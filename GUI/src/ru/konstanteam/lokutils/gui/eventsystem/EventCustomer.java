package ru.konstanteam.lokutils.gui.eventsystem;

public interface EventCustomer<T extends Event> {
    void handle(T event);
}
