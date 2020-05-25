package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import ru.lokincompany.lokutils.input.Mouse;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.objects.Vector2i;
import ru.lokincompany.lokutils.render.FBO;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;
import ru.lokincompany.lokutils.ui.eventsystem.events.ClickType;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;

public class UIMainCanvas extends UICanvas {

    protected UIMainCanvasRender render;

    public UIMainCanvas() {
        render = new UIMainCanvasRender(this);
        setSize(new Size(256, 256));
    }

    @Override
    public void update(UIObject parent) {
        Mouse mouse = inputs.mouse;

        if (area.getRect().inside(mouse.getMousePosition())) {
            boolean pressedStatus = mouse.getPressedStatus();
            boolean lastMousePressed = mouse.getLastMousePressed();

            if (pressedStatus && !lastMousePressed)
                customersContainer.handle(new MouseClickedEvent(mouse.getMousePosition(),
                        ClickType.CLICKED, mouse.buttonID)
                );
            else if (!pressedStatus && lastMousePressed)
                customersContainer.handle(new MouseClickedEvent(
                        mouse.getMousePosition(),
                        ClickType.REALIZED, mouse.buttonID)
                );
        }

        super.update(parent);
    }

    public void render() {
        render.render();
    }
}

class UIMainCanvasRender extends UIRenderPart<UIMainCanvas> {

    public UIMainCanvasRender(UIMainCanvas object) {
        super(object);
    }

    @Override
    public void render() {
        GLContext.getCurrent().getViewTools().pushLook(object.getArea().getRect());

        synchronized (object.updateSync) {
            for (RenderPart renderPart : object.renderParts)
                renderPart.render();
        }

        GLContext.getCurrent().getViewTools().popLook();
    }
}