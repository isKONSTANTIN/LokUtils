package ru.konstanteam.lokutils.ui.objects.margin;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.tools.property.Property;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.eventsystem.CustomersContainer;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.ui.layout.UIAbstractLayout;

public class UIMargin extends UIObject {
    protected UIObject object;
    protected Property<Margin> margin = new Property<>(Margin.PIXEL_MARGIN);

    public UIMargin(UIObject object, Margin startMargin){
        this.object = object;
        this.margin.set(startMargin);

        size.set(() -> {
            Margin margin = margin().get();

            return this.object.size().get().offset(margin.left + margin.right, margin.top + margin.bottom);
        });

        customersContainer.addCustomer(event -> {
            Point position = new Point(margin.get().left, margin.get().top);
            Event localizedEvent = event.relativeTo(position);
            this.object.getCustomersContainer().handle(localizedEvent);
        });

    }

    public UIMargin(UIObject object){
        this.object = object;
    }

    public Property<Margin> margin(){
        return margin;
    }

    @Override
    public UIObject getFocusableObject() {
        return object.getFocusableObject();
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent);

        object.update(parent);
    }

    @Override
    public void init(UIObject parent) {
        super.init(parent);

        object.init(parent);
    }

    @Override
    public void render() {
        GLContext.getCurrent().getViewTools().pushTranslate(new Point(margin.get().left, margin.get().top));
        object.render();
        GLContext.getCurrent().getViewTools().popTranslate();
    }
}
