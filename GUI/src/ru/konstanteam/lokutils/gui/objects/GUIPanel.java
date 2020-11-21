package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.tools.property.Property;
import ru.konstanteam.lokutils.gui.eventsystem.Event;
import ru.konstanteam.lokutils.gui.layout.GUIAbstractLayout;

import static java.lang.Math.min;

public class GUIPanel<T extends GUIAbstractLayout> extends GUIBlackout {
    protected T rootLayout;

    protected Property<Point> canvasPosition;
    protected Property<Size> canvasSize;

    public GUIPanel(T rootLayout) {
        canvasPosition = new Property<>(() -> {
            float pixelsRound = this.getPixelsIndentation();
            return new Point(pixelsRound, pixelsRound);
        });

        canvasSize = new Property<>(() -> {
            float pixelsRound = this.getPixelsIndentation();
            return size().get().relativeTo(pixelsRound * 2, pixelsRound * 2);
        });

        this.rootLayout = rootLayout;
        rootLayout.size().set(canvasSize);
        customersContainer.addCustomer(Event.class, rootLayout.getCustomersContainer());

        size().set(new Size(100, 100));
        setRounded(0.3f);
    }

    public float getPixelsIndentation() {
        Size size = size().get();
        return min(size.width, size.height) * rounded / 5f;
    }

    public T getRootLayout() {
        return rootLayout;
    }

    @Override
    public void update() {
        super.update();

        rootLayout.update();
    }

    @Override
    public void render() {
        GLContext.getCurrent().getViewTools().pushLook(new Rect(canvasPosition.get(), canvasSize.get()));
        rootLayout.render();
        GLContext.getCurrent().getViewTools().popLook();
    }
}