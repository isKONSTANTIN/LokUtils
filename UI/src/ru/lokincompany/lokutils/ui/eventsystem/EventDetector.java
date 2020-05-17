package ru.lokincompany.lokutils.ui.eventsystem;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.ui.UIObject;

public abstract class EventDetector {
    public UIObject uiObject;

    public EventDetector(){

    }

    public void init(UIObject uiObject){
        this.uiObject = uiObject;
    }

    public abstract void update(Inputs inputs);
}
