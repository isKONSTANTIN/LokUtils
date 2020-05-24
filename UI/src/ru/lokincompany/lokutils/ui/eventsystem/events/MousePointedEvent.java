package ru.lokincompany.lokutils.ui.eventsystem.events;

import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.ui.eventsystem.Event;

public class MousePointedEvent extends Event {
    public final Point position;
    public final PointType pointType;

    public MousePointedEvent(Point position, PointType pointType) {
        this.position = position;
        this.pointType = pointType;
    }
}

enum PointType {
    POINTED,
    UNPOINTED
}