package ru.lokincompany.lokutils.ui;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.tools.property.Property;
import ru.lokincompany.lokutils.ui.animation.Animations;
import ru.lokincompany.lokutils.ui.eventsystem.CustomersContainer;
import ru.lokincompany.lokutils.ui.objects.UICanvas;

public class UIObject {
    protected Property<Size> size = new Property<>(Size.ZERO);

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

    public Property<Size> size(){
        return size;
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
        animations.update();
    }

    public void render(){

    }
}
