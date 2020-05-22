package ru.lokincompany.lokutils.ui.eventsystem;

import java.util.ArrayList;

public class EventsContainer {
    protected ArrayList<EventCustomer> customers = new ArrayList<>();

    public void fire(Event event){
        for (EventCustomer customer : customers){
            if (customer.getEventClass() == event.getClass())
                customer.handle(event);
        }
    }

}
