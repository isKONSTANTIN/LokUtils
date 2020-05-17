package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Vector2i;
import ru.lokincompany.lokutils.render.Font;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.tools.Vector2fTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;
import ru.lokincompany.lokutils.ui.animation.Animation;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.EventAction;
import ru.lokincompany.lokutils.ui.eventsystem.EventDetector;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseEvent;
import ru.lokincompany.lokutils.ui.eventsystem.events.SimpleCustomEvent;
import ru.lokincompany.lokutils.ui.positioning.PositioningSetter;

import static org.lwjgl.opengl.GL11.glColor4f;

public class UICheckBox extends UIObject {
    protected UICheckBoxRender render;
    protected Color fillColor;
    protected Vector2f boxSize;
    protected UIText text;

    protected float roundFactor;
    protected boolean status;

    public UICheckBox(){
        render = new UICheckBoxRender(this);

        animations.addAnimation(new Animation("changeStatus") {
            @Override
            public void update() {
                Color end = object.getStyle().getColor(status ? "checkboxFillActive" : "checkboxFillInactive");
                softColorChange(fillColor, end, 2);
                isRun = !softColorChangeDone(fillColor, end);
            }
        });

        this.getEventHandler().putEvent(new SimpleCustomEvent((event) -> {
            Inputs inputs = getInputs();

            if (inputs.mouse.inField(position, boxSize) && inputs.mouse.getPressedStatus() && !inputs.mouse.getLastMousePressed())
                switchStatus();
        }));

        boxSize = new Vector2f(20,20);
        roundFactor = 0.6f;

        text = new UIText();
        text.setPosition(
                new PositioningSetter(this::getPosition)
                .setChildSetter(() -> new Vector2f(boxSize.x + boxSize.x / 4f, boxSize.y / 2f - text.getSize().y / 2f + 1))
        );
        text.setText("CheckBox");
    }

    public UIText getText() {
        return text;
    }

    public UICheckBox setText(UIText text) {
        this.text = text;

        return this;
    }

    public float getRoundFactor() {
        return roundFactor;
    }

    public UICheckBox setRoundFactor(float roundFactor) {
        this.roundFactor = roundFactor;

        return this;
    }

    public UICheckBox switchStatus(){
        setStatus(!status);

        return this;
    }

    public boolean getStatus() {
        return status;
    }

    public UICheckBox setStatus(boolean status) {
        this.status = status;
        animations.startAnimation("changeStatus");

        return this;
    }

    public Vector2f getBoxSize() {
        return boxSize;
    }

    public UICheckBox setBoxSize(Vector2f boxSize) {
        this.boxSize = boxSize;

        return this;
    }

    @Override
    public Vector2f getSize() {
        return Vector2fTools.max(boxSize, text.getSize()).translate(boxSize.x + boxSize.x / 4f,0);
    }

    @Override
    public UIObject setSize(PositioningSetter sizeSetter) {
        return this;
    }

    @Override
    public UIObject setSize(Vector2f size) {
        return this;
    }

    @Override
    public void init(UIObject parent) {
        super.init(parent);

        text.init(parent);
        fillColor = getStyle().getColor("checkboxFillInactive").clone();
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent);

        parent.getCanvasParent().addRenderPart(render);
        text.update(parent);
    }
}

class UICheckBoxRender extends UIRenderPart<UICheckBox> {
    public UICheckBoxRender(UICheckBox object) {
        super(object);
    }

    @Override
    public void render() {
        Color colorStroke = object.getStyle().getColor("checkboxStroke");

        glColor4f(object.fillColor.getRawRed(), object.fillColor.getRawGreen(), object.fillColor.getRawBlue(), object.fillColor.getRawAlpha());
        GLFastTools.drawRoundedSquare(object.getPosition(), object.boxSize, object.roundFactor);

        glColor4f(colorStroke.getRawRed(), colorStroke.getRawGreen(), colorStroke.getRawBlue(), colorStroke.getRawAlpha());
        GLFastTools.drawRoundedHollowSquare(object.getPosition(), object.boxSize, object.roundFactor);
    }
}