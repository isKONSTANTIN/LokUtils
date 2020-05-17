package ru.lokincompany.lokutils.ui.eventsystem;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.ui.UIObject;

public abstract class Event<T extends EventDetector> {
    protected T detector;
    protected UIObject uiObject;

    public Event(T detector){
        this.detector = detector;
    }

    public Event(){
    }

    public void init(UIObject uiObject){
        this.uiObject = uiObject;
        if (detector != null)
            detector.init(uiObject);
    }

    public UIObject getUiObject(){
        return uiObject;
    }

    public abstract void touch(Inputs inputs);
}
