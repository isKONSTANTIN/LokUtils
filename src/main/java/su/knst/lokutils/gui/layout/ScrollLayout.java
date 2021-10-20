package su.knst.lokutils.gui.layout;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.gui.eventsystem.events.MouseScrollEvent;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.render.tools.ViewTools;

public class ScrollLayout extends FreeLayout {
    protected Point scroll = Point.ZERO;
    protected Point scrollMomentum = Point.ZERO;
    protected float momentumFactor = 0.8f;
    protected float scrollFactor = 10f;
    protected Size contentSize = Size.ZERO;

    public ScrollLayout() {
        customersContainer.setCustomer(MouseScrollEvent.class, event -> {
            GUIObject focusedObjectAtOwner = owner != null ? owner.getFocusedObject() : null;

            if (focusedObjectAtOwner == null || !focusedObjectAtOwner.equals(this))
                return;

            scrollMomentum = scrollMomentum.offset(event.scrollDelta);
        });
    }

    @Override
    protected Point getObjectPos(GUIObject object) {
        return super.getObjectPos(object).offset(scroll);
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
    public void calculateAll() {
        if (isValid)
            return;

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

        isValid = true;
    }

    public Point getScroll() {
        return scroll;
    }

    public void setScroll(Point scroll) {
        this.scroll = scroll;
    }

    public Size getContentSize() {
        return contentSize;
    }

    @Override
    public void render() {
        calculateAll();

        Size mySize = size().get();

        float newScrollX;
        float newScrollY;

        if (contentSize.width > mySize.width)
            newScrollX = Math.max(-contentSize.width + mySize.width, Math.min(0, scroll.x + scrollMomentum.x * scrollFactor));
        else
            newScrollX = 0;

        if (contentSize.height > mySize.height)
            newScrollY = Math.max(-contentSize.height + mySize.height, Math.min(0, scroll.y + scrollMomentum.y * scrollFactor));
        else
            newScrollY = 0;

        scrollMomentum = new Point(scrollMomentum.x * momentumFactor, scrollMomentum.y * momentumFactor);
        scroll = new Point(newScrollX, newScrollY);

        ViewTools viewTools = GLContext.getCurrent().getViewTools();

        viewTools.pushLook(new Rect(0, 0, mySize.width, mySize.height));

        for (GUIObject object : objects) {
            Point objectPosition = getObjectPos(object);

            viewTools.pushTranslate(objectPosition);
            object.render();
            viewTools.popTranslate();
        }

        viewTools.popLook();
    }
}
