package ru.lokincompany.lokutils.ui.objects;

import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.tools.property.Property;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.positioning.PositioningLink;

import static java.lang.Math.min;
import static org.lwjgl.opengl.GL11.glColor4f;

public class UIPanel extends UIObject {
    public Color overrideColor;

    protected UICanvas canvas;
    protected float rounded;
    protected Property<Point> canvasPosition;
    protected Property<Size> canvasSize;

    public UIPanel() {
        canvasPosition = new Property<>(() -> {
            float pixelsRound = this.getPixelsIndentation();
            return new Point(pixelsRound, pixelsRound);
        });

        canvasSize = new Property<>(() -> {
            float pixelsRound = this.getPixelsIndentation();
            return size().get().relativeTo(pixelsRound * 2, pixelsRound * 2);
        });

        canvas = new UICanvas();
        canvas.size().set(canvasSize);
        customersContainer.addCustomer(canvas.getCustomersContainer(), Event.class);

        size().set(new Size(100, 100));
        setRounded(0.3f);
    }

    public float getPixelsIndentation() {
        Size size = size().get();
        return min(size.width, size.height) * rounded / 5f;
    }

    public UICanvas getCanvas() {
        return canvas;
    }

    public float getRounded() {
        return rounded;
    }

    public UIPanel setRounded(float rounded) {
        this.rounded = Math.max(Math.min(rounded, 1), 0);
        return this;
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent);

        canvas.update(parent);
    }

    @Override
    public void render() {
        Color color = overrideColor != null ? overrideColor : getStyle().getColor("background");
        glColor4f(color.red, color.green, color.blue, color.alpha);
        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO, size.get()), getRounded());

        GLContext.getCurrent().getViewTools().pushLook(new Rect(canvasPosition.get(), canvasSize.get()));
        canvas.render();
        GLContext.getCurrent().getViewTools().popLook();
    }
}