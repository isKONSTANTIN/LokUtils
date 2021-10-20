package su.knst.lokutils.gui.eventsystem.events;

import su.knst.lokutils.objects.Point;

enum PointType {
    POINTED,
    UNPOINTED
}

public class MousePointedEvent extends Event {
    public final Point position;
    public final PointType pointType;

    public MousePointedEvent(Point position, PointType pointType) {
        this.position = position;
        this.pointType = pointType;
    }
}