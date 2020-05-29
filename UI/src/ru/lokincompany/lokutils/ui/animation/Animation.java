package ru.lokincompany.lokutils.ui.animation;

import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.positioning.AdvancedRect;

import java.util.DoubleSummaryStatistics;

public class Animation<T extends UIObject> {
    protected String name;
    protected boolean isRun;
    protected T object;

    public Animation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void start() {
        isRun = true;
        started();
    }

    public void stop() {
        isRun = false;
        stopped();
    }

    public T getObject() {
        return object;
    }

    public boolean isRun() {
        return isRun;
    }

    public void init(T object) {
        this.object = object;
    }

    protected void setPosition(float x, float y) {
        object.getArea().setPosition(new Point(x, y));
    }

    protected void setSize(float width, float height) {
        object.getArea().setSize(new Size(width, height));
    }

    protected void moveObject(float x, float y) {
        object.getArea().setPosition(object.getArea().getRect().position.offset(x, y));
    }

    protected void stretchObject(float x, float y) {
        object.getArea().setSize(object.getArea().getRect().size.offset(x, y));
    }

    protected void softSetPositionObject(float x, float y, float speed) {
        AdvancedRect rect = object.getArea();
        rect.setPosition(new Point(
                softChange(rect.getPosition().x, x, speed),
                softChange(rect.getPosition().y, y, speed)
        ));
    }

    protected void softSetSizeObject(float width, float height, float speed) {
        AdvancedRect rect = object.getArea();
        rect.setSize(new Size(
                softChange(rect.getSize().width, width, speed),
                softChange(rect.getSize().height, height, speed)
        ));
    }

    public static float softChange(float source, float end, float speed) {
        return source + (end - source) * (speed / 10f);
    }

    public static Color softColorChange(Color source, Color end, float speed) {
        return new Color(
                softChange(source.red, end.red, speed),
                softChange(source.green, end.green, speed),
                softChange(source.blue, end.blue, speed),
                softChange(source.alpha, end.alpha, speed)
        );
    }

    public static boolean softChangeDone(float source, float end, float measurementError) {
        return Math.abs(end - source) < measurementError;
    }

    public static boolean softChangeDone(float source, float end) {
        return softChangeDone(source, end, 0.02f);
    }

    public static boolean softColorChangeDone(Color source, Color end) {
        return softChangeDone(source.red, end.red) &&
                softChangeDone(source.green, end.green) &&
                softChangeDone(source.blue, end.blue) &&
                softChangeDone(source.alpha, end.alpha);
    }

    public void update() {
    }

    public void started() {
    }

    public void stopped() {
    }
}
