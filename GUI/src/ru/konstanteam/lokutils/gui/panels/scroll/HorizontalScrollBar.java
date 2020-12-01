package ru.konstanteam.lokutils.gui.panels.scroll;

import ru.konstanteam.lokutils.gui.eventsystem.events.ClickType;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.gui.layout.ScrollLayout;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.tools.GLFastTools;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;

public class HorizontalScrollBar extends ScrollBar {
    public HorizontalScrollBar(ScrollPanel panel) {
        super(panel);

        size().set(() -> this.panel.size().get().setHeight(BAR_SIZE).relativeTo(4 + BAR_SIZE, 0));

        headSize.set(() -> {
            Size size = size().get();
            ScrollLayout layout = this.panel.layout;

            return size.setWidth(Math.max(size.height, layout.size().get().width / layout.getContentSize().width * size.width)).relativeTo(2, 2);
        });

        headPosition.set(() -> {
            Size size = size().get();
            Size headSize = this.headSize.get();
            ScrollLayout layout = this.panel.layout;

            float offset = layout.getScroll().x / (layout.size().get().width - layout.getContentSize().width) * (size.width - headSize.width);

            return new Point(Math.max(3f, Math.min(offset, size.width - headSize.width + 1f)), 1);
        });

        customersContainer.setCustomer(MouseClickedEvent.class, (event) -> {
            ScrollLayout layout = this.panel.layout;
            Size size = size().get();
            Size headSize = this.headSize.get();

            if (!active() || event.clickType != ClickType.CLICKED)
                return;

            layout.setScroll(layout.getScroll().setX((event.position.x - headSize.width / 2f) / (size.width - headSize.width) * (layout.size().get().width - layout.getContentSize().width)));
        });

        customersContainer.setCustomer(MouseMoveEvent.class, (event) -> {
            ScrollLayout layout = this.panel.layout;
            Size size = size().get();
            Size headSize = this.headSize.get();

            if (!active())
                return;

            layout.setScroll(layout.getScroll().setX((event.lastPosition.x - headSize.width / 2f) / (size.width - headSize.width) * (layout.size().get().width - layout.getContentSize().width)));
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

        Color background = getStyle().getColor("horizontalScrollBarBackground");
        Color head = getStyle().getColor("horizontalScrollBarHead");

        glColor4f(background.red, background.green, background.blue, background.alpha);
        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO.setX(2), size), 1);

        Size headSize = this.headSize.get();
        Point headPosition = this.headPosition.get();

        glColor4f(head.red, head.green, head.blue, head.alpha);
        GLFastTools.drawRoundedSquare(new Rect(headPosition, headSize), 1);
    }
}
