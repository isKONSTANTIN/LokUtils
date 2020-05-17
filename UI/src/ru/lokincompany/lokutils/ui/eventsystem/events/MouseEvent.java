package ru.lokincompany.lokutils.ui.eventsystem.events;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.EventAction;

public class MouseEvent extends Event<MouseEventDetector> {
    protected EventAction<MouseEvent> clickedAction;
    protected EventAction<MouseEvent> pointedAction;
    protected EventAction<MouseEvent> realizedAction;
    protected EventAction<MouseEvent> unClickedAction;
    protected EventAction<MouseEvent> unPointedAction;

    public MouseEvent() {
        super(new MouseEventDetector());
    }

    public MouseEvent setUnClickedAction(EventAction<MouseEvent> unClickedAction) {
        this.unClickedAction = unClickedAction;

        return this;
    }

    public MouseEvent setUnPointedAction(EventAction<MouseEvent> unPointedAction) {
        this.unPointedAction = unPointedAction;

        return this;
    }

    public MouseEvent setClickedAction(EventAction<MouseEvent> eventAction){
        this.clickedAction = eventAction;

        return this;
    }

    public MouseEvent setPointedAction(EventAction<MouseEvent> eventAction){
        this.pointedAction = eventAction;

        return this;
    }

    public MouseEvent setRealizedAction(EventAction<MouseEvent> realizedAction) {
        this.realizedAction = realizedAction;

        return this;
    }

    @Override
    public void touch(Inputs inputs) {
        detector.update(inputs);

        if (clickedAction != null && detector.isClicked()) clickedAction.handle(this);
        if (realizedAction != null && detector.isRealized()) realizedAction.handle(this);
        if (pointedAction != null && detector.isPointed()) pointedAction.handle(this);
        if (unClickedAction != null && detector.isUnClicked()) unClickedAction.handle(this);
        if (unPointedAction != null && detector.isUnPointed()) unPointedAction.handle(this);
    }
}
