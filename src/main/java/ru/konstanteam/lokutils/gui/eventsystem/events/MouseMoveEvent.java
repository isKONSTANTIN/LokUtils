package ru.konstanteam.lokutils.gui.eventsystem.events;

import ru.konstanteam.lokutils.objects.Point;

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

    @Override
    public Event relativeTo(Point position) {
        return new MouseMoveEvent(this.startPosition.relativeTo(position), this.lastPosition.relativeTo(position), this.endPosition.relativeTo(position), type);
    }
}
