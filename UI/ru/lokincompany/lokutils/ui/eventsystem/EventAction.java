package ru.lokincompany.lokutils.ui.eventsystem;

public class EventAction {

    protected EventDetector detector;

    public EventAction(EventDetector detector){
        this.detector = detector;
    }

    public EventAction(EventType type){
        this.detector = EventDetectors.getDetector(type);
    }

    public EventDetector getDetector(){
        return detector;
    }

    public void start(){}
    public void update(){}
    public void stop(){}
}
