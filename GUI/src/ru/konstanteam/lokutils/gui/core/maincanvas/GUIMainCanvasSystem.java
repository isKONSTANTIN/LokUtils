package ru.konstanteam.lokutils.gui.core.maincanvas;

import ru.konstanteam.lokutils.gui.GUIStyle;
import ru.konstanteam.lokutils.gui.core.GUIController;
import ru.konstanteam.lokutils.gui.eventsystem.Event;
import ru.konstanteam.lokutils.gui.layout.GUIAbstractLayout;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Window;

public class GUIMainCanvasSystem<T extends GUIAbstractLayout> extends GUIController {
    protected T layout;

    public GUIMainCanvasSystem(T layout) {
        this.layout = layout;
    }

    public T getLayout() {
        return layout;
    }

    public void setLayout(T layout) {
        this.layout = layout;

        layout.size().set(() -> new Size(window.getResolution()));
    }

    @Override
    public void init(Window window, GUIStyle GUIStyle) {
        super.init(window, GUIStyle);
        layout.setStyle(GUIStyle);
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
