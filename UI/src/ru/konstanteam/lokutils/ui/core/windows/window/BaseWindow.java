package ru.konstanteam.lokutils.ui.core.windows.window;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.ui.UIStyle;
import ru.konstanteam.lokutils.ui.core.windows.UIWindowSystem;
import ru.konstanteam.lokutils.ui.core.windows.bar.BaseWindowBar;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.ui.eventsystem.events.MoveType;
import ru.konstanteam.lokutils.ui.layout.Alignment;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.objects.UIBlackout;

public class BaseWindow extends AbstractWindow<FreeLayout, BaseWindowBar<BaseWindow>> {
    protected Point lastMoveDelta = Point.ZERO;
    protected boolean resizeStatus;

    public void setMinimized(boolean minimized) {
        this.minimized = minimized;
    }

    public void setStyle(UIStyle style) {
        this.style = style;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    protected FreeLayout initLayout() {
        return new FreeLayout();
    }

    @Override
    protected BaseWindowBar<BaseWindow> initBar() {
        BaseWindowBar<BaseWindow> baseWindowBar = new BaseWindowBar<>();
        baseWindowBar.init(this);

        return baseWindowBar;
    }

    @Override
    public void initContent(UIWindowSystem windowSystem) {
        super.initContent(windowSystem);

        UIBlackout blackout = new UIBlackout();
        blackout.size().set(layout.size());
        layout.addObject(blackout, Alignment.TOP_LEFT);
    }

    public void handleMouseMoveEvent(MouseMoveEvent event) {
        if (event.type == MoveType.STARTED && bar.getRect().inside(event.startPosition))
            lastMoveDelta = event.deltaPositionChange;

        if (lastMoveDelta != Point.ZERO) {
            windowSystem.setWindowsPosition(windowSystem.getWindowsPosition(this).offset(event.deltaPositionChange.relativeTo(lastMoveDelta)), this);
            lastMoveDelta = event.deltaPositionChange;
        }

        float distance = new Point(size.width, size.height).distance(event.startPosition);

        if (resizable() && (distance <= 3 && event.type == MoveType.STARTED || event.type == MoveType.CONTINUED && resizeStatus)) {
            if (event.type == MoveType.STARTED)
                windowSystem.bringToFront(this);

            Point deltaPos = event.endPosition.relativeTo(new Rect(Point.ZERO, size).getBottomRightPoint());

            Size newSize = size.offset(deltaPos.x, deltaPos.y);
            newSize = new Size(
                    Math.max(newSize.width, getMinSize().width),
                    Math.max(newSize.height, getMinSize().height)
            );

            setSize(newSize);

            resizeStatus = true;
        }

    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof MouseMoveEvent) {
            handleMouseMoveEvent((MouseMoveEvent) event);
        } else {
            lastMoveDelta = Point.ZERO;
            resizeStatus = false;
        }

        bar.getCustomersContainer().handle(event.relativeTo(bar.getRect().position));

        if (!minimized)
            layout.getCustomersContainer().handle(event);
    }

    @Override
    public void render() {
        bar.update();

        GLContext.getCurrent().getViewTools().pushLook(bar.getRect());
        bar.render();
        GLContext.getCurrent().getViewTools().popLook();

        if (minimized) return;

        layout.update(null);

        GLContext.getCurrent().getViewTools().pushLook(new Rect(Point.ZERO, layout.size().get()), 0);
        layout.render();
        GLContext.getCurrent().getViewTools().popLook();
    }

}
