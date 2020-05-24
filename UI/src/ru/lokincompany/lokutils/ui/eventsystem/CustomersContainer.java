package ru.lokincompany.lokutils.ui.eventsystem;

import ru.lokincompany.lokutils.tools.Removable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomersContainer implements EventCustomer {
    protected HashMap<Class<? extends Event>, EventCustomer> customers = new HashMap<>();
    protected HashMap<Class<? extends Event>, EventHandler> handlers = new HashMap<>();

    public <T extends Event> void setEventHandler(EventHandler<T> eventHandler, Class<T> eventClass){
        handlers.put(eventClass, eventHandler);
    }

    public <T extends Event> Removable addCustomer(EventCustomer<T> customer, Class<T> eventClass){
        customers.put(eventClass, customer);

        return () -> customers.remove(customer);
    }

    @Override
    public void handle(Event event) {
        for (Map.Entry<Class<? extends Event>, EventCustomer> entry : customers.entrySet()) {
            Class<? extends Event> eventClass = event.getClass();
            Class<? extends Event> customerEventClass = entry.getKey();

            if (customerEventClass != Event.class && customerEventClass != eventClass)
                continue;

            EventHandler handler = handlers.get(eventClass);
            Event handledEvent = handler != null ? handler.handle(event) : event;

            entry.getValue().handle(handledEvent);
        }
    }
}
