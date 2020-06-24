package ru.konstanteam.lokutils.ui.eventsystem;

public interface EventCustomer<T extends Event> {
    void handle(T event);
}
