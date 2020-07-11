package ru.konstanteam.lokutils.ui.eventsystem.events;

import ru.konstanteam.lokutils.input.KeyInfo;
import ru.konstanteam.lokutils.ui.eventsystem.Event;

public class KeyTypedEvent extends Event {
    public final KeyInfo key;

    public KeyTypedEvent(KeyInfo key) {
        this.key = key;
    }
}
