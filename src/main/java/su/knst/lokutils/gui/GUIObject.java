package su.knst.lokutils.gui;

import su.knst.lokutils.gui.animation.Animations;
import su.knst.lokutils.gui.eventsystem.CustomersContainer;
import su.knst.lokutils.gui.layout.GUIAbstractLayout;
import su.knst.lokutils.gui.style.GUIObjectAsset;
import su.knst.lokutils.gui.style.GUIStyle;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.tools.property.PropertyBasic;

public abstract class GUIObject {
    protected PropertyBasic<Size> minimumSize = new PropertyBasic<>(Size.ZERO);
    protected PropertyBasic<Size> maximumSize = new PropertyBasic<>(new Size(Float.MAX_VALUE, Float.MAX_VALUE));
    protected PropertyBasic<Size> size = new PropertyBasic<>(minimumSize);

    protected GUIStyle style;
    protected GUIObjectAsset asset;
    protected String name = "UIObject";

    protected Animations animations = new Animations(this);
    protected CustomersContainer customersContainer = new CustomersContainer();

    protected GUIAbstractLayout owner;

    public GUIObject(){
        this.asset = getStyle().asset(this.getClass());
    }

    public GUIObjectAsset getAsset() {
        return asset;
    }

    public GUIAbstractLayout getOwner() {
        return owner;
    }

    public GUIObject getFocusableObject() {
        return this;
    }

    public Animations getAnimations() {
        return animations;
    }

    public String getName() {
        return name;
    }

    public GUIObject setName(String name) {
        this.name = name;

        return this;
    }

    public GUIStyle getStyle() {
        return style != null ? style : (owner != null ? owner.getStyle() : GUIStyle.getDefault());
    }

    public GUIObject setStyle(GUIStyle style) {
        this.style = style;
        this.asset = style.asset(this.getClass());

        return this;
    }

    public PropertyBasic<Size> size() {
        return size;
    }

    public PropertyBasic<Size> minimumSize() {
        return minimumSize;
    }

    public PropertyBasic<Size> maximumSize() {
        return maximumSize;
    }

    public CustomersContainer getCustomersContainer() {
        return customersContainer;
    }

    public void init(GUIAbstractLayout owner) {
        this.owner = owner;
    }

    public void update() {
        if (style == null)
            this.asset = getStyle().asset(this.getClass());

        animations.update(owner.getRefreshRate());
    }

    public abstract void render();
}
