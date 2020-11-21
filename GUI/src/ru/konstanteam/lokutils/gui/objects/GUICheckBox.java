package ru.konstanteam.lokutils.gui.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.tools.Vector2fTools;
import ru.konstanteam.lokutils.tools.property.Property;
import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.animation.Animation;
import ru.konstanteam.lokutils.gui.eventsystem.EventTools;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.gui.layout.GUIAbstractLayout;

import static org.lwjgl.opengl.GL11.glColor4f;

public class GUICheckBox extends GUIObject {
    protected Color fillColor;
    protected Size boxSize;
    protected GUIText text;

    protected Property<Point> textPosition = new Property<>(Point.ZERO);

    protected float roundFactor;
    protected float borderWidth = 1;
    protected boolean status;

    public GUICheckBox() {
        animations.addAnimation(new Animation("changeStatus") {
            @Override
            public void update(double speed) {
                Color end = object.getStyle().getColor(status ? "checkboxFillActive" : "checkboxFillInactive");
                fillColor = softColorChange(fillColor, end, (float)speed * 2);
                isRun = !softColorChangeDone(fillColor, end);
            }
        });

        customersContainer.addCustomer(MouseClickedEvent.class, event -> {
            if (EventTools.realized(event, customersContainer.getLastEvent(MouseClickedEvent.class), new Rect(Point.ZERO, boxSize)))
                switchStatus();
        });

        boxSize = new Size(20, 20);
        roundFactor = 0.6f;

        setText(new GUIText().setText("CheckBox"));

        size.set(() ->
                new Size(Vector2fTools.max(new Vector2f(boxSize.width + borderWidth * 2, boxSize.height + borderWidth * 2), new Vector2f(text.size().get().width, text.size().get().height)).translate(boxSize.width + boxSize.width / 4f, 0))
        );

        textPosition.set(() ->
                new Point(boxSize.width + boxSize.width / 4f, boxSize.height / 2f - text.size().get().height / 2f + 1)
        );
    }

    public GUIText getText() {
        return text;
    }

    public GUICheckBox setText(GUIText text) {
        this.text = text;

        return this;
    }

    public GUICheckBox setText(String text) {
        this.text.setText(text);

        return this;
    }

    public float getRoundFactor() {
        return roundFactor;
    }

    public GUICheckBox setRoundFactor(float roundFactor) {
        this.roundFactor = roundFactor;

        return this;
    }

    public GUICheckBox switchStatus() {
        setStatus(!status);

        return this;
    }

    public boolean getStatus() {
        return status;
    }

    public GUICheckBox setStatus(boolean status) {
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

    public GUICheckBox setBoxSize(Size boxSize) {
        this.boxSize = boxSize;

        return this;
    }

    @Override
    public void init(GUIAbstractLayout owner) {
        super.init(owner);

        text.init(owner);
        fillColor = getStyle().getColor("checkboxFillInactive");
    }

    @Override
    public void update() {
        super.update();

        text.update();
    }

    @Override
    public void render() {
        glColor4f(fillColor.red, fillColor.green, fillColor.blue, fillColor.alpha);
        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO, boxSize), roundFactor);

        if (borderWidth > 0) {
            Color colorStroke = getStyle().getColor("checkboxStroke");

            glColor4f(colorStroke.red, colorStroke.green, colorStroke.blue, colorStroke.alpha);
            GL11.glLineWidth(borderWidth);
            GLFastTools.drawRoundedHollowSquare(new Rect(new Point(borderWidth / 2, borderWidth / 2), boxSize.relativeTo(borderWidth, borderWidth)), roundFactor);
        }

        GLContext.getCurrent().getViewTools().pushTranslate(textPosition.get());
        text.render();
        GLContext.getCurrent().getViewTools().popTranslate();
    }
}
