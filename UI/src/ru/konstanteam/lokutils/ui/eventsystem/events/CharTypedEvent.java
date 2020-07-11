package ru.konstanteam.lokutils.ui.eventsystem.events;

import ru.konstanteam.lokutils.input.KeyInfo;
import ru.konstanteam.lokutils.ui.eventsystem.Event;

public class CharTypedEvent extends Event {
    public final KeyInfo key;

    public CharTypedEvent(KeyInfo key) {
        this.key = key;
    }
}
