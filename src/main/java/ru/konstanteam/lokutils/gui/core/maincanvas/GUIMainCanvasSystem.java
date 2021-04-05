package ru.konstanteam.lokutils.gui.core.maincanvas;

import ru.konstanteam.lokutils.gui.style.GUIStyle;
import ru.konstanteam.lokutils.gui.core.GUIController;
import ru.konstanteam.lokutils.gui.eventsystem.Event;
import ru.konstanteam.lokutils.gui.layout.FreeLayout;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Window;

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
        layout.size().set(() -> new Size(window.getResolution()));
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
