package ru.lokincompany.lokutils.ui.eventsystem;

import ru.lokincompany.lokutils.tools.Removable;

import java.util.ArrayList;

public class CustomersContainer {
    protected ArrayList<EventCustomer> customers = new ArrayList<>();

    public void fire(Event event){
        for (EventCustomer customer : customers){
            if (customer.getEventClass() == event.getClass())
                customer.handle(event);
        }
    }

    public Removable addCustomer(EventCustomer customer){
        customers.add(customer);

        return () -> customers.remove(customer);
    }

}
