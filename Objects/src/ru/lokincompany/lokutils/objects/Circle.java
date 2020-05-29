package ru.lokincompany.lokutils.objects;

import java.util.Currency;

public class Circle {
    public final Point position;
    public final float radius;

    public Circle(Point position, float radius) {
        this.position = position;
        this.radius = radius;
    }

    public Circle setRadius(float radius){
        return new Circle(position, radius);
    }

    public Circle setPosition(Point position){
        return new Circle(position, radius);
    }

    public boolean inside(Rect rect){
        return inside(rect.position) && inside(rect.position.offset(rect.size.width, rect.size.height));
    }

    public boolean inside(Point point) {
        return Math.sqrt(Math.pow(point.x - position.x, 2) + Math.pow(point.y  - position.y, 2)) <= radius;
    }

}
