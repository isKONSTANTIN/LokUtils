package ru.lokincompany.lokutils.ui.eventsystem;

import ru.lokincompany.lokutils.objects.Field;
import ru.lokincompany.lokutils.ui.eventsystem.events.ClickType;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;

public class EventTools {
    public static boolean realized(MouseClickedEvent event, MouseClickedEvent lastEvent, Field field){
        if (lastEvent == null) return false;

        boolean eventClickInside = field.inside(event.position);
        boolean lastEventClickInside = field.inside(lastEvent.position);

        return event.clickType == ClickType.UNCLICKED &&
                lastEvent.clickType == ClickType.CLICKED &&
                eventClickInside && lastEventClickInside;
    }
}
