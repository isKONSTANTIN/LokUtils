package ru.lokincompany.lokutils.ui;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.ui.animation.Animations;
import ru.lokincompany.lokutils.ui.objects.UICanvas;

public abstract class UIObject {

    protected Vector2f position = new Vector2f();
    protected Vector2f size = new Vector2f();
    protected UIStyle style = UIStyle.defaultStyle;
    protected Animations animations = new Animations(this);
    protected String name = "UIObject";

    public Animations getAnimations() {
        return animations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UIStyle getStyle() {
        return style;
    }

    public UIObject setStyle(UIStyle style){
        this.style = style;
        return this;
    }

    public Vector2f getPosition(){
        return position;
    }

    public Vector2f getSize(){
        return size;
    }

    public UIObject setPosition(Vector2f position){
        this.position = position;
        return this;
    }

    public UIObject setSize(Vector2f size){
        this.size = size;
        return this;
    }

    public RenderPart update(UICanvas parent){
        animations.update();

        return null;
    }
}
