package ru.lokincompany.lokutils.ui.eventsystem;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.objects.Vector2i;
import ru.lokincompany.lokutils.ui.UIObject;

public class EventDetectors {

    public static EventDetector getDetector(EventType type){
        switch (type){
            case Click:
                return (object, inputs) ->
                        inputs.mouse.inField(new Vector2i(object.getPosition()), new Vector2i(object.getSize())) &&
                        inputs.mouse.getPressedStatus();
            default:
                return null;
        }
    }

}
