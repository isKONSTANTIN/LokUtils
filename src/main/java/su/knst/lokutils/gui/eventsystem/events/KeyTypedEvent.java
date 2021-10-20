package su.knst.lokutils.gui.eventsystem.events;

import su.knst.lokutils.input.KeyInfo;

public class KeyTypedEvent extends Event {
    public final KeyInfo key;

    public KeyTypedEvent(KeyInfo key) {
        this.key = key;
    }
}
