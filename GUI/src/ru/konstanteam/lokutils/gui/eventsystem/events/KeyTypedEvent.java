package ru.konstanteam.lokutils.gui.eventsystem.events;

import ru.konstanteam.lokutils.input.KeyInfo;
import ru.konstanteam.lokutils.gui.eventsystem.Event;

public class KeyTypedEvent extends Event {
    public final KeyInfo key;

    public KeyTypedEvent(KeyInfo key) {
        this.key = key;
    }
}
