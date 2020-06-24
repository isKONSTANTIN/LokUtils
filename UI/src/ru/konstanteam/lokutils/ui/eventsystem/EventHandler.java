package ru.konstanteam.lokutils.ui.eventsystem;

public interface EventHandler<T extends Event> {
    T handle(T event);
}
