package ru.konstanteam.lokutils.gui.panels.scroll;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.eventsystem.events.ClickType;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseScrollEvent;
import ru.konstanteam.lokutils.gui.layout.ScrollLayout;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.tools.property.Property;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public class VerticalScrollBar extends ScrollBar {
    public VerticalScrollBar(ScrollPanel panel){
        super(panel);

        size().set(() -> this.panel.size().get().setWidth(BAR_SIZE).relativeTo(0, 4 + BAR_SIZE));

        headSize.set(() -> {
            Size size = size().get();
            ScrollLayout layout = this.panel.layout;

            return size.setHeight(Math.max(size.width, layout.size().get().height / layout.getContentSize().height * size.height)).relativeTo(2, 2);
        });

        headPosition.set(() -> {
            Size size = size().get();
            Size headSize = this.headSize.get();
            ScrollLayout layout = this.panel.layout;

            float offset = layout.getScroll().y / (layout.size().get().height - layout.getContentSize().height) * (size.height - headSize.height);

            return new Point(1, Math.max(3f, Math.min(offset, size.height - headSize.height + 1f)));
        });

        customersContainer.setCustomer(MouseClickedEvent.class, (event) -> {
            ScrollLayout layout = this.panel.layout;
            Size size = size().get();
            Size headSize = this.headSize.get();

            if (!active() || event.clickType != ClickType.CLICKED)
                return;

            layout.setScroll(layout.getScroll().setY((event.position.y - headSize.height / 2f) / (size.height - headSize.height) * (layout.size().get().height - layout.getContentSize().height)));
        });

        customersContainer.setCustomer(MouseMoveEvent.class, (event) -> {
            ScrollLayout layout = this.panel.layout;
            Size size = size().get();
            Size headSize = this.headSize.get();

            if (!active())
                return;

            layout.setScroll(layout.getScroll().setY((event.lastPosition.y - headSize.height / 2f) / (size.height - headSize.height) * (layout.size().get().height - layout.getContentSize().height)));
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

        Color background = getStyle().getColor("verticalScrollBarBackground");
        Color head = getStyle().getColor("verticalScrollBarHead");

        Size size = size().get();

        glColor4f(background.red, background.green, background.blue, background.alpha);
        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO.setY(2), size), 1);

        Size headSize = this.headSize.get();
        Point headPosition = this.headPosition.get();

        glColor4f(head.red, head.green, head.blue, head.alpha);
        GLFastTools.drawRoundedSquare(new Rect(headPosition, headSize), 1);
    }
}
