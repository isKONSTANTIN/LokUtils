package ru.konstanteam.lokutils.gui.eventsystem.events;

import ru.konstanteam.lokutils.objects.Point;

public class MouseClickedEvent extends Event {
    public final Point position;
    public final ClickType clickType;
    public final int button;

    public MouseClickedEvent(Point position, ClickType clickType, int button) {
        this.position = position;
        this.button = button;
        this.clickType = clickType;
    }

    @Override
    public Event relativeTo(Point position) {
        return new MouseClickedEvent(this.position.relativeTo(position), clickType, button);
    }

}

