package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;
import ru.lokincompany.lokutils.ui.positioning.PositionSetter;
import ru.lokincompany.lokutils.ui.positioning.SizeSetter;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;

public class UIPanel extends UIObject {

    protected UIPanelRender render;
    protected UICanvas canvas;
    protected float rounded;

    public UIPanel() {
        render = new UIPanelRender(this);
        canvas = (UICanvas) new UICanvas()
                .setSize((SizeSetter)
                        ((SizeSetter) this::getSize).reduceModifier(5))
                .setPosition((PositionSetter)
                        ((PositionSetter) this::getPosition).increaseModifier(5));

        setSize(new Vector2f(100, 100));
        setRounded(0.3f);
    }

    public UICanvas getCanvas() {
        return canvas;
    }

    public UIPanel setRounded(float rounded){
        this.rounded = rounded;
        return this;
    }

    public float getRounded() {
        return rounded;
    }

    @Override
    public void update(UICanvas parent) {
        super.update(parent);

        parent.addRenderPart(render);
        canvas.update(parent);
    }
}

class UIPanelRender extends UIRenderPart<UIPanel> {

    public UIPanelRender(UIPanel panel) {
        super(panel);
    }

    @Override
    public void render() {
        Color color = object.getStyle().getColor("background");
        glColor4f(color.getRawRed(), color.getRawGreen(), color.getRawBlue(), color.getRawAlpha());
        GLFastTools.drawRoundedSquare(object.getPosition(), object.getSize(), object.getRounded());
    }
}