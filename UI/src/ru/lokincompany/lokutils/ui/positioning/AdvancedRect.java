package ru.lokincompany.lokutils.ui.positioning;

import ru.lokincompany.lokutils.objects.Point;

public class AdvancedRect {
    public static PositioningAlgorithm<Point> TOP_LEFT = (object) -> Point.ZERO;
    public static PositioningAlgorithm<Point> TOP_CENTER = (object) -> new Point(object.getLastParent().size().get().width / 2f - object.size().get().width / 2f, 0);
    public static PositioningAlgorithm<Point> TOP_RIGHT = (object) -> new Point(object.getLastParent().size().get().width - object.size().get().width, 0);

    public static PositioningAlgorithm<Point> CENTER_LEFT = (object) -> new Point(0, object.getLastParent().size().get().height / 2 - object.size().get().height / 2);
    public static PositioningAlgorithm<Point> CENTER = (object) -> new Point(object.getLastParent().size().get().width / 2f - object.size().get().width / 2f, object.getLastParent().size().get().height / 2 - object.size().get().height / 2);
    public static PositioningAlgorithm<Point> CENTER_RIGHT = (object) -> new Point(object.getLastParent().size().get().width - object.size().get().width, object.getLastParent().size().get().height / 2 - object.size().get().height / 2);

    public static PositioningAlgorithm<Point> BOTTOM_LEFT = (object) -> new Point(0, object.getLastParent().size().get().height - object.size().get().height);
    public static PositioningAlgorithm<Point> BOTTOM_CENTER = (object) -> new Point(object.getLastParent().size().get().width / 2f - object.size().get().width / 2f, object.getLastParent().size().get().height - object.size().get().height);
    public static PositioningAlgorithm<Point> BOTTOM_RIGHT = (object) -> new Point(object.getLastParent().size().get().width - object.size().get().width, object.getLastParent().size().get().height - object.size().get().height);
}
