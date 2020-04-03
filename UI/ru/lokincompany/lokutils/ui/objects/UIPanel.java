package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;

public class UIPanel extends UIObject {

    protected UIPanelRender render;
    protected float rounded;

    public UIPanel(){
        render = new UIPanelRender(this);

        setSize(new Vector2f(100, 100));
        setRounded(0.3f);
    }

    public UIPanel setRounded(float rounded){
        this.rounded = rounded;
        return this;
    }

    public float getRounded() {
        return rounded;
    }

    @Override
    public RenderPart update(UICanvas parent) {
        super.update(parent);

        return render;
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