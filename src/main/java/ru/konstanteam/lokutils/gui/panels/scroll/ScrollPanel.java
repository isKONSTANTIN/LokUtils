package ru.konstanteam.lokutils.gui.panels.scroll;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.eventsystem.events.Event;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.gui.layout.GUIAbstractLayout;
import ru.konstanteam.lokutils.gui.layout.ScrollLayout;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.tools.property.Property;

public class ScrollPanel extends GUIObject {
    protected VerticalScrollBar vBar;
    protected HorizontalScrollBar hBar;

    protected Property<Point> vBarPos = new Property<>(Point.ZERO);
    protected Property<Point> hBarPos = new Property<>(Point.ZERO);

    protected ScrollLayout layout;

    public ScrollPanel(){
        vBar = new VerticalScrollBar(this);
        hBar = new HorizontalScrollBar(this);

        vBarPos.set(() -> new Point(size.get().width - vBar.size().get().width - 2, 0));
        hBarPos.set(() -> new Point(0, size.get().height - hBar.size().get().height - 2));

        layout = new ScrollLayout();
        layout.size().set(() -> size().get().relativeTo(vBar.active() ? vBar.size().get().width + 2 : 0, hBar.active() ? hBar.size().get().height + 2 : 0));

        customersContainer.setCustomer(Event.class, event -> layout.getCustomersContainer().handle(event));

        customersContainer.setCustomer(MouseClickedEvent.class, event -> {
            if (new Rect(vBarPos.get(), vBar.size().get()).inside(event.position))
                vBar.getCustomersContainer().handle(event.relativeTo(vBarPos.get()));

            if (new Rect(hBarPos.get(), hBar.size().get()).inside(event.position))
                hBar.getCustomersContainer().handle(event.relativeTo(hBarPos.get()));
        });

        customersContainer.setCustomer(MouseMoveEvent.class, event -> {
            if (new Rect(vBarPos.get(), vBar.size().get()).inside(event.startPosition))
                vBar.getCustomersContainer().handle(event.relativeTo(vBarPos.get()));

            if (new Rect(hBarPos.get(), hBar.size().get()).inside(event.startPosition))
                hBar.getCustomersContainer().handle(event.relativeTo(hBarPos.get()));
        });

        size().set(new Size(256, 256));
    }

    public ScrollLayout layout(){
        return layout;
    }

    @Override
    public GUIObject getFocusableObject() {
        return layout;
    }

    @Override
    public void init(GUIAbstractLayout owner) {
        super.init(owner);

        vBar.init(owner);
        hBar.init(owner);
        layout.init(owner);
    }

    @Override
    public void update() {
        super.update();

        vBar.init(owner);
        hBar.init(owner);
        layout.update();
    }

    @Override
    public void render() {
        layout.render();

        GLContext.getCurrent().getViewTools().pushTranslate(vBarPos.get());
        vBar.render();
        GLContext.getCurrent().getViewTools().popTranslate();

        GLContext.getCurrent().getViewTools().pushTranslate(hBarPos.get());
        hBar.render();
        GLContext.getCurrent().getViewTools().popTranslate();
    }
}
