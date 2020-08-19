package ru.konstanteam.lokutils.ui.layout;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.render.tools.ViewTools;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseScrollEvent;

public class ScrollLayout extends FreeLayout {
    protected float scroll;
    protected float scrollMomentum;
    protected float momentumFactor = 0.8f;
    protected float scrollFactor = 2f;

    public ScrollLayout() {
        customersContainer.addCustomer(event -> {
            if (focusedObject == null && lastParent == null)
                return;

            UIObject focusedObjectAtOwner = lastParent != null ? lastParent.getOwner().focusedObject : null;

            if (focusedObjectAtOwner == null || !focusedObjectAtOwner.equals(this))
                return;

            scrollMomentum += event.scrollDelta.y;
        }, MouseScrollEvent.class);
    }

    @Override
    protected Point getObjectPos(UIObject object) {
        return super.getObjectPos(object).offset(0, scroll);
    }

    @Override
    protected Point getLazyObjectPos(UIObject object) {
        return super.getLazyObjectPos(object).offset(0, scroll);
    }

    public float getMomentumFactor() {
        return momentumFactor;
    }

    public void setMomentumFactor(float momentumFactor) {
        this.momentumFactor = momentumFactor;
    }

    public float getScrollFactor() {
        return scrollFactor;
    }

    public void setScrollFactor(float scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    @Override
    public void render() {
        scroll += scrollMomentum * scrollFactor;
        scrollMomentum *= momentumFactor;

        ViewTools viewTools = GLContext.getCurrent().getViewTools();
        Size mySize = size().get();

        viewTools.pushLook(new Rect(0, 0, mySize.width, mySize.height), 0);

        for (UIObject object : objects) {
            Point objectPosition = getObjectPos(object);
            Size objectSize = object.size().get();

            viewTools.pushLook(new Rect(objectPosition.x, objectPosition.y, objectSize.width, objectSize.height));
            object.render();
            viewTools.popLook();
        }

        viewTools.popLook();
    }
}
