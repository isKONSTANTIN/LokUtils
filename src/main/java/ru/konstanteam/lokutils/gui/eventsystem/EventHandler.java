package ru.konstanteam.lokutils.gui.eventsystem;

public interface EventHandler<T extends Event> {
    T handle(T event);
}
