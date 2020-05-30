package ru.lokincompany.lokutils.ui.positioning;

import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.ui.UIObject;

public class AdvancedRect {
    public static PositioningAlgorithm<Point> TOP_LEFT = (oldValue, object) -> Point.ZERO;
    public static PositioningAlgorithm<Point> TOP_CENTER = (oldValue, object) -> new Point(object.getLastParent().getArea().getWidth() / 2f - object.getArea().getWidth() / 2f, 0);
    public static PositioningAlgorithm<Point> TOP_RIGHT = (oldValue, object) -> new Point(object.getLastParent().getArea().getWidth() - object.getArea().getWidth(), 0);

    public static PositioningAlgorithm<Point> CENTER_LEFT = (oldValue, object) -> new Point(0, object.getLastParent().getArea().getHeight() / 2 - object.getArea().getHeight() / 2);
    public static PositioningAlgorithm<Point> CENTER = (oldValue, object) -> new Point(object.getLastParent().getArea().getWidth() / 2f - object.getArea().getWidth() / 2f, object.getLastParent().getArea().getHeight() / 2 - object.getArea().getHeight() / 2);
    public static PositioningAlgorithm<Point> CENTER_RIGHT = (oldValue, object) -> new Point(object.getLastParent().getArea().getWidth() - object.getArea().getWidth(), object.getLastParent().getArea().getHeight() / 2 - object.getArea().getHeight() / 2);

    public static PositioningAlgorithm<Point> BOTTOM_LEFT = (oldValue, object) -> new Point(0, object.getLastParent().getArea().getHeight() - object.getArea().getHeight());
    public static PositioningAlgorithm<Point> BOTTOM_CENTER = (oldValue, object) -> new Point(object.getLastParent().getArea().getWidth() / 2f - object.getArea().getWidth() / 2f, object.getLastParent().getArea().getHeight() - object.getArea().getHeight());
    public static PositioningAlgorithm<Point> BOTTOM_RIGHT = (oldValue, object) -> new Point(object.getLastParent().getArea().getWidth() - object.getArea().getWidth(), object.getLastParent().getArea().getHeight() - object.getArea().getHeight());

    protected Rect rect;
    protected AdvancedRect bindedRect;
    protected PositioningAlgorithm<Point> positionAlgorithm;
    protected PositioningAlgorithm<Size> sizeAlgorithm;

    public AdvancedRect(PositioningAlgorithm<Point> positionAlgorithm, PositioningAlgorithm<Size> sizeAlgorithm) {
        this.rect = Rect.ZERO;

        setPosition(positionAlgorithm);
        setSize(sizeAlgorithm);
    }

    public AdvancedRect(Point position, Size size) {
        this.rect = new Rect(position, size);
    }

    public AdvancedRect(float x, float y, float width, float height) {
        this.rect = new Rect(x, y, width, height);
    }

    public AdvancedRect(){
        this.rect = Rect.ZERO;
    }

    public AdvancedRect setPosition(Point position){
        this.rect = rect.setPosition(position);

        return this;
    }

    public AdvancedRect setSize(Size size){
        this.rect = rect.setSize(size);

        return this;
    }

    public AdvancedRect setPosition(PositioningAlgorithm<Point> positionAlgorithm){
        this.positionAlgorithm = positionAlgorithm;

        return this;
    }

    public AdvancedRect setSize(PositioningAlgorithm<Size> sizeAlgorithm){
        this.sizeAlgorithm = sizeAlgorithm;

        return this;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void bind(AdvancedRect rect){
        this.bindedRect = rect;
    }

    public void update(UIObject object){
        if (bindedRect != null){
            rect = bindedRect.rect;

            return;
        }
        if (positionAlgorithm != null)
            rect = rect.setPosition(positionAlgorithm.calculate(rect.position, object));

        if (sizeAlgorithm != null)
            rect = rect.setSize(sizeAlgorithm.calculate(rect.size, object));
    }

    public float getX() {
        return rect.getX();
    }

    public float getY() {
        return rect.getY();
    }

    public float getWidth() {
        return rect.getWidth();
    }

    public float getHeight() {
        return rect.getHeight();
    }

    public Point getPosition(){
        return rect.position;
    }

    public Size getSize(){
        return rect.size;
    }

    public Rect getRect(){
        return rect;
    }

}
