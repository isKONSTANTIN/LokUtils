package ru.lokincompany.lokutils.ui.eventsystem;

public interface EventCustomer<T extends Event> {
    void handle(T event);
    Class<T> getEventClass();
}
