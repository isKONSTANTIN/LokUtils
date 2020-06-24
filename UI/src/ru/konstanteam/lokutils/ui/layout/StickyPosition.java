package ru.konstanteam.lokutils.ui.layout;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.ui.UIObject;

public enum StickyPosition {
    TOP_LEFT((objectSize, parentSize) -> Point.ZERO),
    TOP_CENTER((objectSize, parentSize) -> new Point(parentSize.width / 2f - objectSize.width / 2f, 0)),
    TOP_RIGHT((objectSize, parentSize) -> new Point(parentSize.width - objectSize.width, 0)),

    CENTER_LEFT((objectSize, parentSize) -> new Point(0, parentSize.height / 2 - objectSize.height / 2)),
    CENTER((objectSize, parentSize) -> new Point(parentSize.width / 2f - objectSize.width / 2f, parentSize.height / 2 - objectSize.height / 2)),
    CENTER_RIGHT((objectSize, parentSize) -> new Point(parentSize.width - objectSize.width, parentSize.height / 2 - objectSize.height / 2)),

    BOTTOM_LEFT((objectSize, parentSize) -> new Point(0, parentSize.height - objectSize.height)),
    BOTTOM_CENTER((objectSize, parentSize) -> new Point(parentSize.width / 2f - objectSize.width / 2f, parentSize.height - objectSize.height)),
    BOTTOM_RIGHT((objectSize, parentSize) -> new Point(parentSize.width - objectSize.width, parentSize.height - objectSize.height));

    Position position;

    StickyPosition(Position position){
        this.position = position;
    }

    public Point getPosition(Size objectSize, Size parentSize){
        return position.calculate(objectSize, parentSize);
    }

    interface Position {
        Point calculate(Size objectSize, Size parentSize);
    }
}
