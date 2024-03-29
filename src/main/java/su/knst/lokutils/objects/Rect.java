package su.knst.lokutils.objects;

import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Rect extends Field {
    public static final Rect ZERO = new Rect(Point.ZERO, Size.ZERO);

    public final Point position;
    public final Size size;

    public Rect(Point position, Size size) {
        this.position = position;
        this.size = size;
    }

    public Rect(Size size) {
        this(Point.ZERO, size);
    }

    public Rect(float x, float y, float width, float height) {
        this(new Point(x, y), new Size(width, height));
    }

    public Rect setPosition(Point position) {
        return new Rect(position, size);
    }

    public Rect setSize(Size size) {
        return new Rect(position, size);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return size.width;
    }

    public float getHeight() {
        return size.height;
    }

    public Point getTopLeftPoint() {
        return position;
    }

    public Point getTopRightPoint() {
        return getTopLeftPoint().offset(size.width, 0);
    }

    public Point getBottomRightPoint() {
        return getTopRightPoint().offset(0, size.height);
    }

    public Point getBottomLeftPoint() {
        return getTopLeftPoint().offset(0, size.height);
    }

    public Point getCenterPoint() {
        return new Point(
                getX() + getWidth() / 2f,
                getY() + getHeight() / 2f
        );
    }

    public Rect relativeTo(Point origin) {
        return offset(-origin.x, -origin.y);
    }

    public Rect offset(Point position) {
        return new Rect(this.position.x + position.x, this.position.y + position.y, size.width, size.height);
    }

    public boolean inside(Rect rect) {
        return inside(rect.position) && inside(rect.position.offset(rect.size.width, rect.size.height));
    }

    public boolean inside(Point point) {
        return (point.x >= this.getX() && point.x <= this.getWidth() + this.getX()) &&
                (point.y >= this.getY() && point.y <= this.getHeight() + this.getY());
    }

    public boolean isIntersect(Rect rect) {
        float x = max(position.x, rect.getX());
        float y = max(position.y, rect.getY());
        float rx = min(position.x + size.width, rect.getX() + rect.getWidth());
        float ry = min(position.y + size.height, rect.getY() + rect.getHeight());

        return rx > x && ry > y;
    }

    public Rect intersect(Rect rect) {
        float x = max(position.x, rect.getX());
        float y = max(position.y, rect.getY());
        float rx = min(position.x + size.width, rect.getX() + rect.getWidth());
        float ry = min(position.y + size.height, rect.getY() + rect.getHeight());

        return rx <= x || ry <= y ? Rect.ZERO : new Rect(x, y, rx - x, ry - y);
    }

    public Point intersect(Point point) {
        float myX = getX();
        float myY = getY();
        float myWidth = getWidth() + myX;
        float myHeight = getHeight() + myY;

        return new Point(
                point.x < myX ? myX : min(point.x, myWidth),
                point.y < myY ? myY : min(point.y, myHeight)
        );
    }

    public Rect merge(Rect rect) {
        return new Rect(
                Math.min(getX(), rect.getX()),
                Math.min(getY(), rect.getY()),
                Math.max(getWidth(), rect.getWidth()),
                Math.max(getHeight(), rect.getHeight())
        );
    }

    public Rect offset(float x, float y) {
        return new Rect(this.position.x + x, this.position.y + y, size.width, size.height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rect rect = (Rect) o;
        return Objects.equals(position, rect.position) &&
                Objects.equals(size, rect.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, size);
    }
}
