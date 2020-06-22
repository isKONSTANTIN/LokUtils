package ru.lokincompany.lokutils.objects;

import java.util.Objects;

public class Point {
    public static Point ZERO = new Point(0,0);

    public final float x;
    public final float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point setX(float x){
        return new Point(x, y);
    }

    public Point setY(float y){
        return new Point(x, y);
    }

    public Point relativeTo(Point origin) {
        return relativeTo(origin.x, origin.y);
    }

    public Point relativeTo(float x, float y) {
        return offset(-x, -y);
    }

    public Point offset(Point point) {
        return offset(point.x, point.y);
    }

    public Point offset(float x, float y) {
        return new Point(this.x + x, this.y + y);
    }

    public float distance(Point point){
        return distance(point.x, point.y);
    }

    public float distance(float x, float y){
        return (float)Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Float.compare(point.x, x) == 0 &&
                Float.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
