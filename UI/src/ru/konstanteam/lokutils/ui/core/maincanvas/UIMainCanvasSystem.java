package ru.konstanteam.lokutils.ui.core.maincanvas;

import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.ui.core.UIController;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.layout.UIAbstractLayout;

public class UIMainCanvasSystem extends UIController {
    protected UIAbstractLayout layout;

    public UIMainCanvasSystem(Window window) {
        super(window);
    }

    public UIAbstractLayout getLayout() {
        return layout;
    }

    public <T extends UIAbstractLayout> void setLayout(T layout) {
        this.layout = layout;

        layout.size().set(() -> new Size(window.getResolution()));
    }

    @Override
    public void update() {
        Event event = checkEvent();
        if (event != null)
            layout.getCustomersContainer().handle(event);

        layout.update(null);
    }

    @Override
    public void render() {
        layout.render();
    }
}
