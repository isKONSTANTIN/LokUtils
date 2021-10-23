package su.knst.lokutils.gui.panels.scroll;

import su.knst.lokutils.gui.eventsystem.events.*;
import su.knst.lokutils.gui.eventsystem.events.ClickType;
import su.knst.lokutils.gui.eventsystem.events.MouseClickedEvent;
import su.knst.lokutils.gui.eventsystem.events.MouseMoveEvent;
import su.knst.lokutils.gui.layout.ScrollLayout;
import su.knst.lokutils.objects.*;
import su.knst.lokutils.render.tools.GLFastTools;
import su.knst.lokutils.gui.eventsystem.events.MoveType;

import static org.lwjgl.opengl.GL11.*;

public class VerticalScrollBar extends ScrollBar {
    public VerticalScrollBar(ScrollPanel panel){
        super(panel);

        size().track(() -> this.panel.size().get().setWidth(BAR_SIZE).relativeTo(0, 4 + BAR_SIZE));

        headSize.track(() -> {
            Size size = size().get();
            ScrollLayout layout = this.panel.layout;

            return size.setHeight(Math.max(size.width, layout.size().get().height / layout.getContentSize().height * size.height)).relativeTo(2, 2);
        });

        headPosition.track(() -> {
            Size size = size().get();
            Size headSize = this.headSize.get();
            ScrollLayout layout = this.panel.layout;

            float offset = layout.getScroll().y / (layout.size().get().height - layout.getContentSize().height) * (size.height - headSize.height);

            return new Point(1, Math.max(3f, Math.min(offset, size.height - headSize.height + 1f)));
        });

        customersContainer.setCustomer(MouseClickedEvent.class, (event) -> {
            ScrollLayout layout = this.panel.layout;
            Size size = size().get();
            Rect head = new Rect(headPosition.get(), headSize.get());

            if (!active() || event.clickType != ClickType.CLICKED)
                return;

            if (!head.inside(event.position))
                layout.setScroll(layout.getScroll().setY((event.position.y - head.size.height / 2f) / (size.height - head.size.height) * (layout.size().get().height - layout.getContentSize().height)));
        });

        customersContainer.setCustomer(MouseMoveEvent.class, (event) -> {
            ScrollLayout layout = this.panel.layout;
            Size size = size().get();
            Rect head = new Rect(headPosition.get(), headSize.get());

            if (event.type == MoveType.STARTED)
                startMoveBarState = head;

            if (!active())
                return;

            layout.setScroll(layout.getScroll().setY((event.endPosition.y - (event.startPosition.y - startMoveBarState.position.y)) / (size.height - head.size.height) * (layout.size().get().height - layout.getContentSize().height)));
        });
    }

    @Override
    public boolean active() {
        ScrollLayout layout = this.panel.layout;

        return layout.getContentSize().height > this.panel.size().get().height;
    }

    @Override
    public void render() {
        if (!active())
            return;

        Color background = asset.color("background");
        Color head = asset.color("head");

        Size size = size().get();

        glColor4f(background.red, background.green, background.blue, background.alpha);
        GLFastTools.drawSquircle(new Squircle(Point.ZERO.setY(2), size, 1));

        Size headSize = this.headSize.get();
        Point headPosition = this.headPosition.get();

        glColor4f(head.red, head.green, head.blue, head.alpha);
        GLFastTools.drawSquircle(new Squircle(headPosition, headSize, 1));
    }
}
