package ru.konstanteam.lokutils.ui.eventsystem.events;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.ui.eventsystem.Event;

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