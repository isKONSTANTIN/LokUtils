package su.knst.lokutils.gui.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.gui.animation.Animation;
import su.knst.lokutils.gui.eventsystem.EventTools;
import su.knst.lokutils.gui.eventsystem.events.MouseClickedEvent;
import su.knst.lokutils.gui.layout.GUIAbstractLayout;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.render.tools.GLFastTools;
import su.knst.lokutils.tools.Vector2fTools;
import su.knst.lokutils.tools.property.PropertyBasic;

import static org.lwjgl.opengl.GL11.glColor4f;

public class GUICheckBox extends GUIObject {
    protected float filledRadius;
    protected Size boxSize;
    protected GUIText text;

    protected PropertyBasic<Point> textPosition = new PropertyBasic<>(Point.ZERO);

    protected float roundFactor;
    protected float borderWidth = 1;
    protected boolean status;

    public GUICheckBox(GUIText text) {
        animations.addAnimation(new Animation("changeStatus") {
            @Override
            public void update(double speed) {
                float end = status ? boxSize.width - borderWidth - 4 : 0;

                filledRadius = softChange(filledRadius, end, (float) speed * 2);

                isRun = !softChangeDone(filledRadius, end);
            }
        });

        customersContainer.setCustomer(MouseClickedEvent.class, event -> {
            if (EventTools.realized(event, customersContainer.getLastEvent(MouseClickedEvent.class), new Rect(Point.ZERO, boxSize)))
                switchStatus();
        });

        boxSize = new Size(20, 20);
        roundFactor = 0.6f;

        setText(text);

        minimumSize().track(() ->
                new Size(Vector2fTools.max(new Vector2f(boxSize.width + borderWidth * 2, boxSize.height + borderWidth * 2), new Vector2f(text.size().get().width, text.size().get().height)).translate(boxSize.width + boxSize.width / 4f, 0))
        );

        textPosition.track(() ->
                new Point(boxSize.width + boxSize.width / 4f, boxSize.height / 2f - text.size().get().height / 2f + 1)
        );
    }

    public GUICheckBox(String text){
        this(new GUIText(text));
    }

    public GUICheckBox(){
        this("Check box");
    }

    public GUIText getText() {
        return text;
    }

    public GUICheckBox setText(GUIText text) {
        this.text = text;

        return this;
    }

    public GUICheckBox setText(String text) {
        this.text.string().set(text);

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
    }

    @Override
    public void update() {
        super.update();

        text.update();
    }

    @Override
    public void render() {
        if (filledRadius > 0){
            Color fillColor = asset.color("fill");
            glColor4f(fillColor.red, fillColor.green, fillColor.blue, fillColor.alpha);
            GLFastTools.drawRoundedSquare(new Rect(new Point(boxSize.width / 2 - filledRadius / 2f, boxSize.height / 2 - filledRadius/ 2f), new Size(filledRadius, filledRadius)), roundFactor);
        }

        if (borderWidth > 0) {
            Color colorStroke = asset.color("stroke");

            glColor4f(colorStroke.red, colorStroke.green, colorStroke.blue, colorStroke.alpha);
            GL11.glLineWidth(borderWidth);
            GLFastTools.drawRoundedHollowSquare(new Rect(Point.ZERO, boxSize), roundFactor);
        }

        GLContext.getCurrent().getViewTools().pushTranslate(textPosition.get());
        text.render();
        GLContext.getCurrent().getViewTools().popTranslate();
    }
}
