package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.positioning.PositioningLink;

import static java.lang.Math.min;
import static org.lwjgl.opengl.GL11.glColor4f;

public class UIPanel extends UIObject {
    public Color overrideColor;

    protected UIPanelRender render;
    protected UICanvas canvas;
    protected float rounded;

    public UIPanel() {
        render = new UIPanelRender(this);

        PositioningLink<Point> canvasPosition = () -> {
            float pixelsRound = this.getPixelsIndentation();
            return this.getArea().getPosition().offset(pixelsRound, pixelsRound);
        };

        PositioningLink<Size> canvasSize = () -> {
            float pixelsRound = this.getPixelsIndentation();
            return this.getArea().getSize().relativeTo(pixelsRound * 2, pixelsRound * 2);
        };

        canvas = (UICanvas) new UICanvas().setPosition(canvasPosition).setSize(canvasSize);
        customersContainer.addCustomer(canvas.getCustomersContainer(), Event.class);

        setSize(new Size(100, 100));
        setRounded(0.3f);
    }

    public float getPixelsIndentation() {
        Size size = getArea().getSize();
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

        parent.getCanvasParent().addRenderPart(render);
        canvas.update(parent);
    }
}

class UIPanelRender extends UIRenderPart<UIPanel> {

    public UIPanelRender(UIPanel panel) {
        super(panel);
    }

    @Override
    public void render() {
        Color color = object.overrideColor != null ? object.overrideColor : object.getStyle().getColor("background");
        glColor4f(color.red, color.green, color.blue, color.alpha);
        GLFastTools.drawRoundedSquare(object.getArea().getRect(), object.getRounded());
    }
}