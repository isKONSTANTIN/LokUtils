package ru.konstanteam.lokutils.ui.eventsystem.events;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.ui.eventsystem.Event;

public class MouseClickedEvent extends Event {
    public final Point position;
    public final int button;
    public final ClickType clickType;

    public MouseClickedEvent(Point position, ClickType clickType, int button) {
        this.position = position;
        this.button = button;
        this.clickType = clickType;
    }

    public MouseClickedEvent offset(Point position){
        return new MouseClickedEvent(this.position.offset(position), clickType, button);
    }

    public MouseClickedEvent relativeTo(Point position){
        return new MouseClickedEvent(this.position.relativeTo(position), clickType, button);
    }

}

