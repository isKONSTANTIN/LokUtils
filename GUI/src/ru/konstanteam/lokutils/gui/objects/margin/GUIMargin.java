package ru.konstanteam.lokutils.gui.objects.margin;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.tools.property.Property;
import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.eventsystem.Event;
import ru.konstanteam.lokutils.gui.layout.GUIAbstractLayout;

public class GUIMargin extends GUIObject {
    protected GUIObject object;
    protected Property<Margin> margin = new Property<>(Margin.PIXEL_MARGIN);

    public GUIMargin(GUIObject object, Margin startMargin){
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

    public GUIMargin(GUIObject object){
        this.object = object;
    }

    public Property<Margin> margin(){
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
