package ru.konstanteam.lokutils.gui.objects.margin;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.eventsystem.events.Event;
import ru.konstanteam.lokutils.gui.layout.GUIAbstractLayout;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.tools.property.PropertyBasic;

public class GUIMargin extends GUIObject {
    protected GUIObject object;
    protected PropertyBasic<Margin> margin = new PropertyBasic<>(Margin.PIXEL_MARGIN);

    public GUIMargin(GUIObject object, Margin startMargin) {
        this.object = object;
        this.margin.set(startMargin);

        minimumSize().track(() -> {
            Margin margin = margin().get();

            return this.object.minimumSize().get().offset(margin.left + margin.right, margin.top + margin.bottom);
        }, margin(), object.minimumSize());

        size().track(() -> {
            Margin margin = margin().get();

            return this.object.size().get().offset(margin.left + margin.right, margin.top + margin.bottom);
        }, margin(), object.size());

        maximumSize().track(() -> {
            Margin margin = margin().get();
            Size maxObjectSize = this.object.maximumSize().get();

            boolean widthIsMax = maxObjectSize.width == Float.MAX_VALUE;
            boolean heightIsMax = maxObjectSize.height == Float.MAX_VALUE;

            return maxObjectSize.offset(widthIsMax ? 0 : margin.left + margin.right, heightIsMax ? 0 : margin.top + margin.bottom);
        }, margin(), object.maximumSize());

        customersContainer.setCustomer(event -> {
            Point position = new Point(margin.get().left, margin.get().top);
            Event localizedEvent = event.relativeTo(position);
            this.object.getCustomersContainer().handle(localizedEvent);
        });

    }

    public GUIMargin(GUIObject object) {
        this(object, Margin.PIXEL_MARGIN);
    }

    public PropertyBasic<Margin> margin() {
        return margin;
    }

    @Override
    public GUIObject getFocusableObject() {
        return object.getFocusableObject();
    }

    @Override
    public void update() {
        super.update();

        object.update();

        //size.checkParentChanges();
        //minimumSize.checkParentChanges();
        //maximumSize.checkParentChanges();
    }

    @Override
    public void init(GUIAbstractLayout owner) {
        super.init(owner);

        object.init(owner);
    }

    @Override
    public void render() {
        GLContext.getCurrent().getViewTools().pushTranslate(new Point(margin.get().left, margin.get().top));
        object.render();
        GLContext.getCurrent().getViewTools().popTranslate();
    }
}
