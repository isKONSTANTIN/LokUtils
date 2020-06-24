package ru.konstanteam.lokutils.ui.objects;

import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.tools.property.Property;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.layout.BaseLayout;
import ru.konstanteam.lokutils.ui.layout.UIAbstractLayout;

import static java.lang.Math.min;
import static org.lwjgl.opengl.GL11.glColor4f;

public class UIPanel<T extends UIAbstractLayout> extends UIObject {
    public Color overrideColor;

    protected T rootLayout;
    protected float rounded;
    protected Property<Point> canvasPosition;
    protected Property<Size> canvasSize;

    public UIPanel(T rootLayout) {
        canvasPosition = new Property<>(() -> {
            float pixelsRound = this.getPixelsIndentation();
            return new Point(pixelsRound, pixelsRound);
        });

        canvasSize = new Property<>(() -> {
            float pixelsRound = this.getPixelsIndentation();
            return size().get().relativeTo(pixelsRound * 2, pixelsRound * 2);
        });

        this.rootLayout = rootLayout;
        rootLayout.size().set(canvasSize);
        customersContainer.addCustomer(rootLayout.getCustomersContainer(), Event.class);

        size().set(new Size(100, 100));
        setRounded(0.3f);
    }

    public float getPixelsIndentation() {
        Size size = size().get();
        return min(size.width, size.height) * rounded / 5f;
    }

    public T getRootLayout() {
        return rootLayout;
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

        rootLayout.update(parent);
    }

    @Override
    public void render() {
        Color color = overrideColor != null ? overrideColor : getStyle().getColor("background");
        glColor4f(color.red, color.green, color.blue, color.alpha);
        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO, size.get()), getRounded());

        GLContext.getCurrent().getViewTools().pushLook(new Rect(canvasPosition.get(), canvasSize.get()));
        rootLayout.render();
        GLContext.getCurrent().getViewTools().popLook();
    }
}