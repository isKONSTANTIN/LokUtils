package ru.lokincompany.lokutils.ui.eventsystem;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.ui.UIObject;

import java.util.HashMap;
import java.util.Map;

public class EventHandler {

    protected HashMap<String, EventAction> events = new HashMap<>();
    protected HashMap<EventAction, Boolean> eventsStatus = new HashMap<>();

    public EventAction getEvent(String name) {
        return events.get(name);
    }

    public EventHandler addEvent(String name, EventAction action) {
        events.put(name, action);

        return this;
    }

    public EventHandler removeEvent(String name) {
        events.remove(name);

        return this;
    }

    public void update(UIObject object, Inputs inputs) {
        for (Map.Entry<String, EventAction> item : events.entrySet()) {
            EventAction action = item.getValue();

            boolean actionStatus = action.getDetector().detect(object, inputs);
            boolean lastStatus = eventsStatus.getOrDefault(action, false);

            if (actionStatus && !lastStatus) {
                action.start();
            } else if (!actionStatus && lastStatus) {
                action.stop();
            } else if (actionStatus) {
                action.update();
            }

            eventsStatus.put(action, actionStatus);
        }
    }

}
