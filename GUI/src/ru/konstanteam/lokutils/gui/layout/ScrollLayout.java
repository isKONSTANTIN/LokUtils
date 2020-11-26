package ru.konstanteam.lokutils.gui.layout;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseScrollEvent;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.render.tools.ViewTools;

public class ScrollLayout extends FreeLayout {
    protected float scroll;
    protected float scrollMomentum;
    protected float momentumFactor = 0.8f;
    protected float scrollFactor = 2f;
    protected Size contentSize = Size.ZERO;

    public ScrollLayout() {
        customersContainer.addCustomer(MouseScrollEvent.class, event -> {
            GUIObject focusedObjectAtOwner = owner != null ? owner.focusedObject : null;

            if (focusedObjectAtOwner == null || !focusedObjectAtOwner.equals(this))
                return;

            scrollMomentum += event.scrollDelta.y;
        });
    }

    @Override
    protected Point getObjectPos(GUIObject object) {
        return super.getObjectPos(object).offset(0, scroll);
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
    protected void setInvalidStatus() {
        super.setInvalidStatus();
    }

    @Override
    protected void calculateAll() {
        float x = 0;
        float y = 0;

        for (GUIObject object : objects) {
            Point objectPosition = super.getObjectPos(object);
            Size objectSize = object.size().get();

            Point bottomRightPoint = new Rect(objectPosition, objectSize).getBottomRightPoint();

            x = Math.max(x, bottomRightPoint.x);
            y = Math.max(y, bottomRightPoint.y);
        }

        contentSize = new Size(x, y);
    }

    @Override
    public void render() {
        Size mySize = size().get();

        if (contentSize.height > mySize.height)
            scroll = Math.max(-contentSize.height + mySize.height, Math.min(0, scroll + scrollMomentum * scrollFactor));
        else
            scroll = 0;

        scrollMomentum *= momentumFactor;

        ViewTools viewTools = GLContext.getCurrent().getViewTools();

        viewTools.pushLook(new Rect(0, 0, mySize.width, mySize.height), 0);

        for (GUIObject object : objects) {
            Point objectPosition = getObjectPos(object);

            viewTools.pushTranslate(objectPosition);
            object.render();
            viewTools.popTranslate();
        }

        viewTools.popLook();
    }
}
