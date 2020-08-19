package ru.konstanteam.lokutils.ui;

import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.tools.property.Property;
import ru.konstanteam.lokutils.ui.animation.Animations;
import ru.konstanteam.lokutils.ui.eventsystem.CustomersContainer;
import ru.konstanteam.lokutils.ui.layout.UIAbstractLayout;

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

    public UIAbstractLayout getOwner() {
        return lastParent.getOwner();
    }

    public UIObject getFocusableObject() {
        return this;
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
        return style != null ? style : (lastParent != null ? lastParent.getOwner().getStyle() : UIStyle.getDefault());
    }

    public UIObject setStyle(UIStyle style) {
        this.style = style;
        return this;
    }

    public Property<Size> size() {
        return size;
    }

    public CustomersContainer getCustomersContainer() {
        return customersContainer;
    }

    public void init(UIObject parent) {
        lastParent = parent;
    }

    public void update(UIObject parent) {
        lastParent = parent;
        animations.update();
    }

    public void render() {

    }
}
