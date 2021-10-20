package su.knst.lokutils.gui.eventsystem;

import su.knst.lokutils.gui.eventsystem.events.ClickType;
import su.knst.lokutils.gui.eventsystem.events.MouseClickedEvent;
import su.knst.lokutils.objects.Field;

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
