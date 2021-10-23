package su.knst.lokutils.gui.panels.scroll;

import su.knst.lokutils.gui.eventsystem.events.ClickType;
import su.knst.lokutils.gui.eventsystem.events.MouseClickedEvent;
import su.knst.lokutils.gui.eventsystem.events.MouseMoveEvent;
import su.knst.lokutils.gui.eventsystem.events.MoveType;
import su.knst.lokutils.gui.layout.ScrollLayout;
import su.knst.lokutils.objects.*;
import su.knst.lokutils.render.tools.GLFastTools;

import static org.lwjgl.opengl.GL11.glColor4f;

public class HorizontalScrollBar extends ScrollBar {
    public HorizontalScrollBar(ScrollPanel panel) {
        super(panel);

        size().track(() -> this.panel.size().get().setHeight(BAR_SIZE).relativeTo(4 + BAR_SIZE, 0));

        headSize.track(() -> {
            Size size = size().get();
            ScrollLayout layout = this.panel.layout;

            return size.setWidth(Math.max(size.height, layout.size().get().width / layout.getContentSize().width * size.width)).relativeTo(2, 2);
        });

        headPosition.track(() -> {
            Size size = size().get();
            Size headSize = this.headSize.get();
            ScrollLayout layout = this.panel.layout;

            float offset = layout.getScroll().x / (layout.size().get().width - layout.getContentSize().width) * (size.width - headSize.width);

            return new Point(Math.max(3f, Math.min(offset, size.width - headSize.width + 1f)), 1);
        });

        customersContainer.setCustomer(MouseClickedEvent.class, (event) -> {
            ScrollLayout layout = this.panel.layout;
            Size size = size().get();
            Rect head = new Rect(headPosition.get(), headSize.get());

            if (!active() || event.clickType != ClickType.CLICKED)
                return;

            if (!head.inside(event.position))
                layout.setScroll(layout.getScroll().setX((event.position.x - head.size.width / 2f) / (size.width - head.size.width) * (layout.size().get().width - layout.getContentSize().width)));
        });

        customersContainer.setCustomer(MouseMoveEvent.class, (event) -> {
            ScrollLayout layout = this.panel.layout;
            Size size = size().get();
            Rect head = new Rect(headPosition.get(), headSize.get());

            if (event.type == MoveType.STARTED)
                startMoveBarState = head;

            if (!active())
                return;

            layout.setScroll(layout.getScroll().setX((event.endPosition.x - (event.startPosition.x - startMoveBarState.position.x)) / (size.width - head.size.width) * (layout.size().get().width - layout.getContentSize().width)));
       });
    }

    @Override
    public boolean active() {
        ScrollLayout layout = this.panel.layout;

        return layout.getContentSize().width > this.panel.size().get().width;
    }


    @Override
    public void render() {
        if (!active())
            return;

        Size size = size().get();

        Color background = asset.color("background");
        Color head = asset.color("head");

        glColor4f(background.red, background.green, background.blue, background.alpha);
        GLFastTools.drawSquircle(new Squircle(Point.ZERO.setX(2), size, 1));

        Size headSize = this.headSize.get();
        Point headPosition = this.headPosition.get();

        glColor4f(head.red, head.green, head.blue, head.alpha);
        GLFastTools.drawSquircle(new Squircle(headPosition, headSize, 1));
    }
}
