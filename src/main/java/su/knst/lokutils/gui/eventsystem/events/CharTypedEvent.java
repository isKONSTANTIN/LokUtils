package su.knst.lokutils.gui.eventsystem.events;

import su.knst.lokutils.input.KeyInfo;

public class CharTypedEvent extends Event {
    public final KeyInfo key;

    public CharTypedEvent(KeyInfo key) {
        this.key = key;
    }
}
