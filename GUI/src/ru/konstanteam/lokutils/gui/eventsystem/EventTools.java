package ru.konstanteam.lokutils.gui.eventsystem;

import ru.konstanteam.lokutils.objects.Field;
import ru.konstanteam.lokutils.gui.eventsystem.events.ClickType;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseClickedEvent;

public class EventTools {
    public static boolean realized(MouseClickedEvent event, MouseClickedEvent lastEvent, Field field) {
        if (lastEvent == null) return false;

        boolean eventClickInside = field.inside(event.position);
        boolean lastEventClickInside = field.inside(lastEvent.position);

        return event.clickType == ClickType.UNCLICKED &&
                lastEvent.clickType == ClickType.CLICKED &&
                eventClickInside && lastEventClickInside;
    }
}
