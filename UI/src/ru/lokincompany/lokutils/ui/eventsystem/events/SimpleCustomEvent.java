package ru.lokincompany.lokutils.ui.eventsystem.events;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.EventAction;
import ru.lokincompany.lokutils.ui.eventsystem.EventDetector;

public class SimpleCustomEvent extends Event {
    protected EventAction<SimpleCustomEvent> eventActionAndDetector;
    protected Inputs inputs;

    public SimpleCustomEvent(EventAction<SimpleCustomEvent> eventActionAndDetector){
        this.eventActionAndDetector = eventActionAndDetector;
    }

    public Inputs getInputs(){
        return inputs;
    }

    @Override
    public void touch(Inputs inputs) {
        this.inputs = inputs;

        eventActionAndDetector.handle(this);
    }
}