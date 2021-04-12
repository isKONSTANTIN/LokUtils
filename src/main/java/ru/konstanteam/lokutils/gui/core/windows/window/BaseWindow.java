package ru.konstanteam.lokutils.gui.core.windows.window;

import ru.konstanteam.lokutils.gui.style.GUIStyle;
import ru.konstanteam.lokutils.gui.core.windows.GUIWindowSystem;
import ru.konstanteam.lokutils.gui.core.windows.bar.BaseWindowBar;
import ru.konstanteam.lokutils.gui.eventsystem.events.Event;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.gui.eventsystem.events.MoveType;
import ru.konstanteam.lokutils.gui.layout.Alignment;
import ru.konstanteam.lokutils.gui.layout.FreeLayout;
import ru.konstanteam.lokutils.gui.objects.GUIBlackout;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.tools.property.Property;

public class BaseWindow extends GUIWindow {
    protected final Property<Boolean> minimized = new Property<>(false);
    protected final Property<Boolean> resizable = new Property<>(false);
    protected final Property<Boolean> closable = new Property<>(false);
    protected final Property<Size> contentSize = new Property<>(Size.ZERO);
    protected Point lastMoveDelta = Point.ZERO;
    protected boolean resizeStatus;
    protected FreeLayout layout;
    protected BaseWindowBar<BaseWindow> bar;

    public BaseWindow() {
        minimumSize().set(() -> Size.max(layout.minimumSize().get().offset(contentOffset().get().x, contentOffset().get().y), bar.minimumSize().get()));

        size().set(() -> {
            Point offset = contentOffset().get();

            return (minimized.get() ? contentSize.get().setHeight(0) : contentSize.get()).offset(offset.x, offset.y);
        });

        contentSize.set(new Size(300, 300));
    }

    public Property<Boolean> closable() {
        return closable;
    }

    public Property<Boolean> resizable() {
        return resizable;
    }

    public Property<Boolean> minimized() {
        return minimized;
    }

    @Override
    public GUIStyle getStyle() {
        return GUIStyle.getDefault();
    }

    public FreeLayout getLayout() {
        return layout;
    }

    public BaseWindowBar<BaseWindow> getBar() {
        return bar;
    }

    @Override
    public Property<Size> contentSize() {
        return contentSize;
    }

    public void init(GUIWindowSystem windowSystem) {
        super.init(windowSystem);

        bar = new BaseWindowBar<>();
        bar.init(this);
        contentOffset.set(() -> bar.getRect().getBottomRightPoint().setX(0));

        layout = new FreeLayout();
        layout.size().set(contentSize);

        layout.minimumSize().set(new Size(50, 50));

        layout.addObject(new GUIBlackout(), Alignment.TOP_LEFT);
    }

    public boolean handleMouseMoveEvent(MouseMoveEvent event) {
        if (event.type == MoveType.STARTED && bar.getRect().inside(event.startPosition))
            lastMoveDelta = event.deltaPositionChange;

        if (lastMoveDelta != Point.ZERO) {
            windowSystem.setWindowsPosition(windowSystem.getWindowsPosition(this).offset(event.deltaPositionChange.relativeTo(lastMoveDelta)), this);
            lastMoveDelta = event.deltaPositionChange;

            return true;
        }

        Size contentSize = this.contentSize.get();
        Size size = this.size.get();

        float distance = new Point(size.width, size.height).distance(event.startPosition);

        if (!(resizable().get() && (distance <= 3 && event.type == MoveType.STARTED || event.type == MoveType.CONTINUED && resizeStatus)))
            return false;

        if (event.type == MoveType.STARTED)
            windowSystem.bringToFront(this);

        Point deltaPos = event.endPosition.relativeTo(new Rect(Point.ZERO, size).getBottomRightPoint());

        Size newSize = contentSize.offset(deltaPos.x, deltaPos.y);
        newSize = new Size(
                Math.max(newSize.width, minimumSize().get().width),
                Math.max(newSize.height, minimumSize().get().height)
        );

        this.contentSize.set(newSize);

        resizeStatus = true;

        return true;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof MouseMoveEvent && handleMouseMoveEvent((MouseMoveEvent) event)) {
            return;
        } else {
            lastMoveDelta = Point.ZERO;
            resizeStatus = false;
        }

        bar.getCustomersContainer().handle(event);

        if (!minimized.get())
            layout.getCustomersContainer().handle(event.relativeTo(contentOffset.get()));
    }

    @Override
    public void render() {
        bar.update();

        GLContext.getCurrent().getViewTools().pushLook(bar.getRect());
        bar.render();
        GLContext.getCurrent().getViewTools().popLook();

        if (minimized.get()) return;

        layout.update();

        GLContext.getCurrent().getViewTools().pushLook(new Rect(contentOffset.get(), layout.size().get()));
        layout.render();
        GLContext.getCurrent().getViewTools().popLook();
    }

}
