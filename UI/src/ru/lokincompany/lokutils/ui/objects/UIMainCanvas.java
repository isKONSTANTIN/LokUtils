package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import ru.lokincompany.lokutils.input.Mouse;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Vector2i;
import ru.lokincompany.lokutils.render.FBO;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;
import ru.lokincompany.lokutils.ui.eventsystem.events.ClickType;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;

public class UIMainCanvas extends UICanvas {

    protected UIMainCanvasRender render;
    protected int multisampleSamples = 16;

    public UIMainCanvas() {
        render = new UIMainCanvasRender(this);
        setSize(new Vector2f(256, 256));
    }

    public int getMultisampleSamples() {
        return multisampleSamples;
    }

    public UIMainCanvas setMultisampleSamples(int multisampleSamples) {
        this.multisampleSamples = multisampleSamples;

        return this;
    }

    @Override
    public void update(UIObject parent) {
        Mouse mouse = inputs.mouse;

        if (mouse.inField(position, size)) {

            boolean pressedStatus = mouse.getPressedStatus();
            boolean lastMousePressed = mouse.getLastMousePressed();

            if (pressedStatus)
                System.out.println("C");

            if (pressedStatus && !lastMousePressed)
                customersContainer.handle(new MouseClickedEvent(new Point(
                        mouse.getMousePosition().getX(),
                        mouse.getMousePosition().getY()),
                        ClickType.CLICKED, mouse.buttonID)
                );
            else if (!pressedStatus && lastMousePressed)
                customersContainer.handle(new MouseClickedEvent(new Point(
                        mouse.getMousePosition().getX(),
                        mouse.getMousePosition().getY()),
                        ClickType.REALIZED, mouse.buttonID)
                );
        }

        super.update(parent);
    }

    public FBO getFbo() {
        return render.getFbo();
    }

    public void render() {
        render.render();
    }
}

class UIMainCanvasRender extends UIRenderPart<UIMainCanvas> {

    protected FBO fbo;

    public UIMainCanvasRender(UIMainCanvas object) {
        super(object);
        fbo = new FBO().setResolution(new Vector2i((int) object.getSize().x, (int) object.getSize().y)).setMultisampled(true).setMultisampleSamples(object.getMultisampleSamples()).applyChanges();
    }

    public FBO getFbo() {
        return fbo;
    }

    @Override
    public void render() {
        boolean fboBeenChanged = false;

        if ((int) object.getSize().x != fbo.getResolution().getX() || (int) object.getSize().y != fbo.getResolution().getY()) {
            fbo.setResolution(new Vector2i((int) object.getSize().x, (int) object.getSize().y));
            fboBeenChanged = true;
        }

        if (fbo.getMultisampleSamples() != object.getMultisampleSamples()) {
            fbo.setMultisampleSamples(object.getMultisampleSamples());
            fboBeenChanged = true;
        }

        if (fboBeenChanged)
            fbo.applyChanges();

        fbo.bind();

        ViewTools.setOrtho2DView(new Vector4f(0, object.getSize().getX(), object.getSize().getY(), 0));

        synchronized (object.updateSync) {
            for (RenderPart renderPart : object.renderParts)
                renderPart.render();
        }

        fbo.unbind();
    }
}