package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.tools.Vector2fTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;
import ru.lokincompany.lokutils.ui.animation.Animation;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.EventCustomer;
import ru.lokincompany.lokutils.ui.eventsystem.events.ClickType;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.lokincompany.lokutils.ui.positioning.AdvancedRect;

import static org.lwjgl.opengl.GL11.glColor4f;

public class UICheckBox extends UIObject {
    protected UICheckBoxRender render;
    protected Color fillColor;
    protected Size boxSize;
    protected UIText text;

    protected float roundFactor;
    protected boolean status;

    public UICheckBox(){
        render = new UICheckBoxRender(this);

        animations.addAnimation(new Animation("changeStatus") {
            @Override
            public void update() {
                Color end = object.getStyle().getColor(status ? "checkboxFillActive" : "checkboxFillInactive");
                fillColor = softColorChange(fillColor, end, 2);
                isRun = !softColorChangeDone(fillColor, end);
            }
        });

        customersContainer.addCustomer(event -> {
            if (event.clickType != ClickType.REALIZED) return;

            if (area.getRect().setSize(boxSize).inside(event.position))
                switchStatus();

        }, MouseClickedEvent.class);

        boxSize = new Size(20,20);
        roundFactor = 0.6f;

        text = new UIText();
        text.setPosition(
                () -> this.getArea().getPosition().offset(boxSize.width + boxSize.width / 4f, boxSize.height / 2f - text.getArea().getHeight() / 2f + 1)
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

    public Size getBoxSize() {
        return boxSize;
    }

    public UICheckBox setBoxSize(Size boxSize) {
        this.boxSize = boxSize;

        return this;
    }

    @Override
    public AdvancedRect getArea() {
        Size textSize = text.getArea().getSize();
        return super.getArea().setSize(new Size(Vector2fTools.max(new Vector2f(boxSize.width, boxSize.height), new Vector2f(textSize.width, textSize.height)).translate(boxSize.width + boxSize.width / 4f,0)));
    }

    @Override
    public void init(UIObject parent) {
        super.init(parent);

        text.init(parent);
        fillColor = getStyle().getColor("checkboxFillInactive");
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

        glColor4f(object.fillColor.red, object.fillColor.green, object.fillColor.blue, object.fillColor.alpha);
        GLFastTools.drawRoundedSquare(object.getArea().getRect().setSize(object.boxSize), object.roundFactor);

        glColor4f(colorStroke.red, colorStroke.green, colorStroke.blue, colorStroke.alpha);
        GLFastTools.drawRoundedHollowSquare(object.getArea().getRect().setSize(object.boxSize), object.roundFactor);
    }
}