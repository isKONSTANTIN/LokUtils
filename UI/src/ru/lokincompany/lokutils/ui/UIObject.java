package ru.lokincompany.lokutils.ui;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.ui.animation.Animations;
import ru.lokincompany.lokutils.ui.eventsystem.CustomersContainer;
import ru.lokincompany.lokutils.ui.objects.UICanvas;
import ru.lokincompany.lokutils.ui.positioning.AdvancedRect;
import ru.lokincompany.lokutils.ui.positioning.PositioningAlgorithm;
import ru.lokincompany.lokutils.ui.positioning.PositioningLink;

public class UIObject {
    protected AdvancedRect area = new AdvancedRect();

    protected UIStyle style;
    protected String name = "UIObject";

    protected Animations animations = new Animations(this);
    protected CustomersContainer customersContainer = new CustomersContainer();

    protected UIObject lastParent;

    public UIObject getLastParent() {
        return lastParent;
    }

    public UICanvas getCanvasParent() {
        return lastParent.getCanvasParent();
    }

    public Animations getAnimations() {
        return animations;
    }

    public String getName() {
        return name;
    }

    public UIObject setName(String name) {
        this.name = name;

        return this;
    }

    public UIStyle getStyle() {
        return style != null ? style : (lastParent != null ? lastParent.getCanvasParent().getStyle() : null);
    }

    public UIObject setStyle(UIStyle style) {
        this.style = style;
        return this;
    }

    public AdvancedRect getArea() {
        return area;
    }

    public UIObject bindArea(AdvancedRect area) {
        this.area.bind(area);

        return this;
    }

    public UIObject setPosition(PositioningLink<Point> positionAlgorithm){
        this.area.setPosition(positionAlgorithm);

        return this;
    }

    public UIObject setSize(PositioningLink<Size> sizeAlgorithm){
        this.area.setSize(sizeAlgorithm);

        return this;
    }

    public UIObject setPosition(PositioningAlgorithm<Point> positionAlgorithm){
        this.area.setPosition(positionAlgorithm);

        return this;
    }

    public UIObject setSize(PositioningAlgorithm<Size> sizeAlgorithm){
        this.area.setSize(sizeAlgorithm);

        return this;
    }

    public UIObject setPosition(Point position){
        this.area.setPosition(position);

        return this;
    }

    public UIObject setSize(Size size){
        this.area.setSize(size);

        return this;
    }

    public Inputs getInputs() {
        return lastParent.getInputs();
    }

    public CustomersContainer getCustomersContainer(){
        return customersContainer;
    }

    public void init(UIObject parent){
        lastParent = parent;
    }

    public void update(UIObject parent) {
        lastParent = parent;

        area.update(this);
        animations.update();
    }
}
