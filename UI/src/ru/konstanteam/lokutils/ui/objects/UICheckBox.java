package ru.konstanteam.lokutils.ui.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.tools.Vector2fTools;
import ru.konstanteam.lokutils.tools.property.Property;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.animation.Animation;
import ru.konstanteam.lokutils.ui.eventsystem.EventTools;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseClickedEvent;

import static org.lwjgl.opengl.GL11.glColor4f;

public class UICheckBox extends UIObject {
    protected Color fillColor;
    protected Size boxSize;
    protected UIText text;

    protected Property<Point> textPosition = new Property<>();

    protected float roundFactor;
    protected float borderWidth = 1;
    protected boolean status;

    public UICheckBox(){
        animations.addAnimation(new Animation("changeStatus") {
            @Override
            public void update() {
                Color end = object.getStyle().getColor(status ? "checkboxFillActive" : "checkboxFillInactive");
                fillColor = softColorChange(fillColor, end, 2);
                isRun = !softColorChangeDone(fillColor, end);
            }
        });

        customersContainer.addCustomer(event -> {
            if (EventTools.realized(event, customersContainer.getLastEvent(MouseClickedEvent.class), new Rect(Point.ZERO, boxSize)))
                switchStatus();
        }, MouseClickedEvent.class);

        boxSize = new Size(20,20);
        roundFactor = 0.6f;

        setText(new UIText().setText("CheckBox"));

        size.set(() ->
                new Size(Vector2fTools.max(new Vector2f(boxSize.width + borderWidth * 2, boxSize.height + borderWidth * 2), new Vector2f(text.size().get().width, text.size().get().height)).translate(boxSize.width + boxSize.width / 4f,0))
        );

        textPosition.set(() ->
                new Point(boxSize.width + boxSize.width / 4f, boxSize.height / 2f - text.size().get().height / 2f + 1)
        );
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

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        if (borderWidth < 0)
            throw new IllegalArgumentException("Border width cannot be less zero!");
        this.borderWidth = borderWidth;
    }

    public Size getBoxSize() {
        return boxSize;
    }

    public UICheckBox setBoxSize(Size boxSize) {
        this.boxSize = boxSize;

        return this;
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

        text.update(parent);
    }

    @Override
    public void render() {
        glColor4f(fillColor.red, fillColor.green, fillColor.blue, fillColor.alpha);
        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO, boxSize), roundFactor);

        if (borderWidth > 0){
            Color colorStroke = getStyle().getColor("checkboxStroke");

            glColor4f(colorStroke.red, colorStroke.green, colorStroke.blue, colorStroke.alpha);
            GL11.glLineWidth(borderWidth);
            GLFastTools.drawRoundedHollowSquare(new Rect(new Point(borderWidth / 2, borderWidth / 2), boxSize.relativeTo(borderWidth , borderWidth)), roundFactor);
        }

        GLContext.getCurrent().getViewTools().pushLook(new Rect(textPosition.get(), text.size().get()));
        text.render();
        GLContext.getCurrent().getViewTools().popLook();
    }
}
