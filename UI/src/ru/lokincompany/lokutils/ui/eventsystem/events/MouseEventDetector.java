package ru.lokincompany.lokutils.ui.eventsystem.events;
/*
import ru.lokincompany.lokutils.input.Inputs;

public class MouseEventDetector extends EventDetector {
    protected boolean clicked;
    protected boolean pointed;
    protected boolean realized;
    protected boolean unClicked;
    protected boolean unPointed;

    private boolean lastPointed;

    @Override
    public void update(Inputs inputs) {
        lastPointed = pointed;
        boolean inField = inputs.mouse.inField(uiObject.getPosition(), uiObject.getSize());

        pointed = inField;
        clicked = inField && inputs.mouse.getPressedStatus();
        unClicked = !inputs.mouse.getPressedStatus() && inputs.mouse.getLastMousePressed();
        realized = unClicked && inField;
        unPointed = !pointed && lastPointed;
    }

    public boolean isRealized() {
        return realized;
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
}*/
