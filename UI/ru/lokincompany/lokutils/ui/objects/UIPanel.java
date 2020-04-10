package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;
import ru.lokincompany.lokutils.ui.positioning.PositioningSetter;

import static java.lang.Math.min;
import static org.lwjgl.opengl.GL11.glColor4f;

public class UIPanel extends UIObject {

    public Color overrideColor;

    protected UIPanelRender render;
    protected UICanvas canvas;
    protected float rounded;

    public UIPanel() {
        render = new UIPanelRender(this);

        PositioningSetter canvasPosition = new PositioningSetter(() -> {
            Vector2f position = this.getPosition();
            float pixelsRound = this.getPixelsIndentation();
            position.x += pixelsRound;
            position.y += pixelsRound;
            return position;
        });

        PositioningSetter canvasSize = new PositioningSetter(() -> {
            Vector2f size = this.getSize();
            float pixelsRound = this.getPixelsIndentation();
            size.x -= pixelsRound * 2;
            size.y -= pixelsRound * 2;
            return size;
        });

        canvas = (UICanvas) new UICanvas().setPosition(canvasPosition).setSize(canvasSize);

        setSize(new Vector2f(100, 100));
        setRounded(0.3f);
    }

    public float getPixelsIndentation(){
        return min(size.x, size.y) * rounded / 5f;
    }

    public UICanvas getCanvas() {
        return canvas;
    }

    public UIPanel setRounded(float rounded){
        this.rounded = Math.max(Math.min(rounded, 1), 0);
        return this;
    }

    public float getRounded() {
        return rounded;
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
        glColor4f(color.getRawRed(), color.getRawGreen(), color.getRawBlue(), color.getRawAlpha());
        GLFastTools.drawRoundedSquare(object.getPosition(), object.getSize(), object.getRounded());
    }
}