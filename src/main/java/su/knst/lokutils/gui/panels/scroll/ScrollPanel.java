package su.knst.lokutils.gui.panels.scroll;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.gui.eventsystem.events.Event;
import su.knst.lokutils.gui.eventsystem.events.MouseClickedEvent;
import su.knst.lokutils.gui.eventsystem.events.MouseMoveEvent;
import su.knst.lokutils.gui.layout.GUIAbstractLayout;
import su.knst.lokutils.gui.layout.ScrollLayout;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.tools.property.PropertyBasic;

public class ScrollPanel extends GUIObject {
    protected VerticalScrollBar vBar;
    protected HorizontalScrollBar hBar;

    protected PropertyBasic<Point> vBarPos = new PropertyBasic<>(Point.ZERO);
    protected PropertyBasic<Point> hBarPos = new PropertyBasic<>(Point.ZERO);

    protected ScrollLayout layout;

    public ScrollPanel(){
        vBar = new VerticalScrollBar(this);
        hBar = new HorizontalScrollBar(this);

        vBarPos.track(() -> new Point(size.get().width - vBar.size().get().width - 2, 0));
        hBarPos.track(() -> new Point(0, size.get().height - hBar.size().get().height - 2));

        layout = new ScrollLayout();
        layout.size().track(() -> size().get().relativeTo(vBar.active() ? vBar.size().get().width + 2 : 0, hBar.active() ? hBar.size().get().height + 2 : 0));

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
