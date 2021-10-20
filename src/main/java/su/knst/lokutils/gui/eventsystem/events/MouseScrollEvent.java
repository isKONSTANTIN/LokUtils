package su.knst.lokutils.gui.eventsystem.events;

import su.knst.lokutils.objects.Point;

public class MouseScrollEvent extends Event {
    public final Point scrollDelta;

    public MouseScrollEvent(Point scrollDelta) {
        this.scrollDelta = scrollDelta;
    }
}
