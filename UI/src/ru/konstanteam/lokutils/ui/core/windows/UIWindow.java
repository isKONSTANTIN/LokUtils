package ru.konstanteam.lokutils.ui.core.windows;

import org.lwjgl.opengl.GL11;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.ui.UIStyle;
import ru.konstanteam.lokutils.ui.core.windows.bar.UIAbstractWindowBar;
import ru.konstanteam.lokutils.ui.core.windows.bar.UIBaseWindowBar;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.ui.eventsystem.events.MoveType;
import ru.konstanteam.lokutils.ui.layout.UIAbstractLayout;

public class UIWindow<T extends UIAbstractLayout, R extends UIAbstractWindowBar> {
    protected Size contentSize = new Size(150, 150);
    protected Size minContentSize = new Size(50, 50);

    protected Point position = Point.ZERO;
    protected Point lastMoveDelta = Point.ZERO;

    protected R bar;
    protected T rootLayout;

    protected UIWindowSystem windowSystem;
    protected UIStyle style;
    protected boolean minimized;

    public UIWindow(T rootLayout, R bar) {
        this.rootLayout = rootLayout;
        this.bar = bar;

        this.rootLayout.size().set(() -> contentSize);
    }

    public UIWindow(T rootLayout) {
        this(rootLayout, (R) new UIBaseWindowBar());
    }

    public boolean canClose() {
        return true;
    }

    public UIWindowSystem getWindowSystem() {
        return windowSystem;
    }

    public UIStyle getStyle() {
        return style;
    }

    public void setStyle(UIStyle style) {
        this.style = style;
    }

    public T getLayout() {
        return rootLayout;
    }

    public R getBar() {
        return bar;
    }

    public Rect getField() {
        return new Rect(position, contentSize).merge(bar.getRect().offset(position));
    }

    public Size getContentSize() {
        return contentSize;
    }

    public void setSize(Size contentSize) {
        this.contentSize = contentSize;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void init(UIWindowSystem windowSystem) {
        this.windowSystem = windowSystem;

        if (this.style == null)
            this.style = windowSystem.getStyle();

        bar.init(this);
    }

    public void handleBarEvent(Event event) {
        if (event instanceof MouseMoveEvent) {
            MouseMoveEvent mouseMoveEvent = (MouseMoveEvent) event;

            if (mouseMoveEvent.type == MoveType.STARTED) {
                lastMoveDelta = mouseMoveEvent.deltaPositionChange;
            }
            if (lastMoveDelta != Point.ZERO) {
                position = position.offset(mouseMoveEvent.deltaPositionChange.relativeTo(lastMoveDelta));
                lastMoveDelta = mouseMoveEvent.deltaPositionChange;
            }
        } else {
            lastMoveDelta = Point.ZERO;
            bar.getCustomersContainer().handle(event);
        }
    }

    public boolean isMinimized() {
        return minimized;
    }

    public void setMinimized(boolean minimized) {
        this.minimized = minimized;
    }

    public Size getMinContentSize() {
        return minContentSize;
    }

    public void handleContentEvent(Event event) {
        if (!minimized)
            rootLayout.getCustomersContainer().handle(event);
    }

    public void renderBar() {
        bar.update();

        bar.render();
    }

    public void renderContent() {
        if (minimized) return;

        Rect contentField = new Rect(Point.ZERO, contentSize);
        Color background = rootLayout.getStyle().getColor("windowContentBackground");

        GL11.glColor4f(background.red, background.green, background.blue, background.alpha);
        GLFastTools.drawSquare(contentField);

        rootLayout.update(null);

        GLContext.getCurrent().getViewTools().pushLook(new Rect(Point.ZERO, contentSize));
        rootLayout.render();
        GLContext.getCurrent().getViewTools().popLook();
    }

}
