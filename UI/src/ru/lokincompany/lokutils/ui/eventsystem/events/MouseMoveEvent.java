package ru.lokincompany.lokutils.ui.eventsystem.events;

import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.ui.eventsystem.Event;

public class MouseMoveEvent extends Event {
    public final Point startPosition;
    public final Point endPosition;
    public final Point lastPosition;
    public final Point deltaPositionChange;

    public final MoveType type;

    public MouseMoveEvent(Point startPosition, Point lastPosition, Point endPosition, MoveType type) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.lastPosition = lastPosition;
        this.type = type;

        this.deltaPositionChange = endPosition.relativeTo(startPosition);
    }
}
