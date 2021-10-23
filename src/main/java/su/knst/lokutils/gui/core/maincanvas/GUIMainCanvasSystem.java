package su.knst.lokutils.gui.core.maincanvas;

import su.knst.lokutils.gui.style.GUIStyle;
import su.knst.lokutils.gui.core.GUIController;
import su.knst.lokutils.gui.eventsystem.events.Event;
import su.knst.lokutils.gui.layout.FreeLayout;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.Window;

public class GUIMainCanvasSystem extends GUIController {
    protected FreeLayout layout;

    public FreeLayout getLayout() {
        return layout;
    }

    @Override
    public void init(Window window) {
        super.init(window);

        layout = new FreeLayout();

        layout.setStyle(GUIStyle.getDefault());
        layout.size().track(window::getResolution);
    }

    @Override
    public void update() {
        Event event = checkEvent();
        if (event != null) {
            layout.getCustomersContainer().handle(event);
        }

        layout.update();
    }

    @Override
    public void render() {
        layout.render();
    }
}
