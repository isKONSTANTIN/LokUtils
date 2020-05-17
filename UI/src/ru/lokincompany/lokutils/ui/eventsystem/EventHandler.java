package ru.lokincompany.lokutils.ui.eventsystem;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.ui.UIObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventHandler {
    protected HashMap<Class<? extends Event>, ArrayList<Event>> events = new HashMap<>();
    protected UIObject object;

    public EventHandler(UIObject object){
        this.object = object;
    }

    public Removable putEvent(Event event){
        event.init(object);

        Class<? extends Event> eventClass = event.getClass();

        if (!events.containsKey(eventClass)) events.put(eventClass, new ArrayList<>());
        events.get(eventClass).add(event);

        return () -> events.get(eventClass).remove(event);
    }

    public void update(Inputs inputs){
        for (Map.Entry<Class<? extends Event>, ArrayList<Event>> eventEntry : events.entrySet())
            for(Event event : eventEntry.getValue()) event.touch(inputs);
    }
}
