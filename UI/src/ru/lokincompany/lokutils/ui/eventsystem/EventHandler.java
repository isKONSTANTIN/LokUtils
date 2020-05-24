package ru.lokincompany.lokutils.ui.eventsystem;

public interface EventHandler<T extends Event> {
    T handle(T event);
}
