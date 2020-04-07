package ru.lokincompany.lokutils.ui;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.input.Keyboard;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.ui.animation.Animations;
import ru.lokincompany.lokutils.ui.objects.UICanvas;
import ru.lokincompany.lokutils.ui.positioning.PositionSetter;
import ru.lokincompany.lokutils.ui.positioning.SizeSetter;

public class UIObject {

    protected Vector2f position = new Vector2f();
    protected Vector2f size = new Vector2f();
    protected UIStyle style = UIStyle.getDefault();
    protected String name = "UIObject";

    protected Animations animations = new Animations(this);
    protected EventHandler eventHandler;

    protected PositionSetter positionSetter;
    protected SizeSetter sizeSetter;

    public PositionSetter getPositionSetter() {
        return positionSetter;
    }

    public UIObject setPositionSetter(PositionSetter positionSetter) {
        this.positionSetter = positionSetter;

        return this;
    }

    public SizeSetter getSizeSetter() {
        return sizeSetter;
    }

    public UIObject setSizeSetter(SizeSetter sizeSetter) {
        this.sizeSetter = sizeSetter;

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

    protected void updateEvents(Inputs inputs){
        if (eventHandler != null)
            eventHandler.update(this, inputs);

        if (positionSetter != null)
            positionSetter.update(this);

        if (sizeSetter != null)
            sizeSetter.update(this);

        animations.update();
    }

    public RenderPart update(UICanvas parent){
        updateEvents(parent.getInputs());

        return null;
    }
}
