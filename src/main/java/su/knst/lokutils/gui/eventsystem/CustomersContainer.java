package su.knst.lokutils.gui.eventsystem;

import su.knst.lokutils.gui.eventsystem.events.Event;
import su.knst.lokutils.tools.Removable;

import java.util.HashMap;
import java.util.Map;

public class CustomersContainer implements EventCustomer {
    protected HashMap<Class<? extends Event>, EventCustomer> customers = new HashMap<>();
    protected HashMap<Class<? extends Event>, EventHandler> handlers = new HashMap<>();
    protected HashMap<Class<? extends Event>, Event> lastEvents = new HashMap<>();

    public <T extends Event> void setEventHandler(EventHandler<T> eventHandler, Class<T> eventClass) {
        handlers.put(eventClass, eventHandler);
    }

    public <T extends Event> Removable setCustomer(Class<T> eventClass, EventCustomer<T> customer) {
        customers.put(eventClass, customer);

        return () -> customers.remove(customer);
    }

    public <T extends Event> Removable setCustomer(EventCustomer<T> customer) {
        return setCustomer(Event.class, (EventCustomer<Event>) customer);
    }

    public <T extends Event> T getLastEvent(Class<T> eventClass) {
        return (T) lastEvents.get(eventClass);
    }

    @Override
    public void handle(Event event) {
        Class<? extends Event> eventClass = event.getClass();

        EventHandler handler = handlers.get(eventClass);
        Event handledEvent = handler != null ? handler.handle(event) : event;

        for (Map.Entry<Class<? extends Event>, EventCustomer> entry : customers.entrySet()) {
            Class<? extends Event> customerEventClass = entry.getKey();

            if (customerEventClass != Event.class && customerEventClass != eventClass)
                continue;

            entry.getValue().handle(handledEvent);
        }

        lastEvents.put(handledEvent.getClass(), handledEvent);
    }
}
