package ru.lokincompany.lokutils.ui.eventsystem.events;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.ui.eventsystem.EventDetector;

public class MouseEventDetector extends EventDetector {
    protected boolean clicked;
    protected boolean pointed;
    protected boolean unClicked;
    protected boolean unPointed;

    private boolean lastPointed;

    @Override
    public void update(Inputs inputs) {
        lastPointed = pointed;

        pointed = inputs.mouse.inField(uiObject.getPosition(),uiObject.getSize());
        clicked = inputs.mouse.getPressedStatus() && !inputs.mouse.getLastMousePressed() && pointed;

        unClicked = !inputs.mouse.getPressedStatus() && !clicked;
        unPointed = !pointed && lastPointed;
    }

    public boolean isUnClicked() {
        return unClicked;
    }

    public boolean isUnPointed() {
        return unPointed;
    }

    public boolean isClicked() {
        return clicked;
    }

    public boolean isPointed() {
        return pointed;
    }
}
