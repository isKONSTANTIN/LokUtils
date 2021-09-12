package ru.konstanteam.lokutils.gui.panels.scroll;

import ru.konstanteam.lokutils.gui.eventsystem.events.*;
import ru.konstanteam.lokutils.gui.layout.ScrollLayout;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.tools.GLFastTools;

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
        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO.setY(2), size), 1);

        Size headSize = this.headSize.get();
        Point headPosition = this.headPosition.get();

        glColor4f(head.red, head.green, head.blue, head.alpha);
        GLFastTools.drawRoundedSquare(new Rect(headPosition, headSize), 1);
    }
}
