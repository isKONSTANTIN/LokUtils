package ru.lokincompany.lokutils.ui.eventsystem;

public interface EventAction<T extends Event> {
    void handle(T event);
}
