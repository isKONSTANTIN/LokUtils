package ru.konstanteam.lokutils.gui.objects.button;

import ru.konstanteam.lokutils.gui.animation.Animation;
import ru.konstanteam.lokutils.gui.eventsystem.EventTools;
import ru.konstanteam.lokutils.gui.eventsystem.events.ClickType;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.gui.layout.FreeLayout;
import ru.konstanteam.lokutils.gui.layout.GUIAbstractLayout;
import ru.konstanteam.lokutils.gui.objects.GUIPanel;
import ru.konstanteam.lokutils.gui.objects.GUIText;
import ru.konstanteam.lokutils.gui.objects.margin.GUIMargin;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;

import static ru.konstanteam.lokutils.gui.layout.Alignment.CENTER;

public class GUIButton extends GUIPanel<FreeLayout> {

    protected GUIText text;
    protected GUIMargin margin;
    protected ButtonAction action;

    public GUIButton() {
        super(new FreeLayout());
        this.text = new GUIText().setText("Button");
        this.margin = new GUIMargin(text);
        this.getRootLayout().addObject(margin, CENTER);

        customersContainer.addCustomer(MouseClickedEvent.class, event -> {
            if (event.clickType == ClickType.CLICKED && new Rect(Point.ZERO, size().get()).inside(event.position)) {
                getAnimations().stopAll();
                getAnimations().startAnimation("pressed");
            } else {
                getAnimations().addAnimationToTaskList("unpressed");

                if (EventTools.realized(event, customersContainer.getLastEvent(MouseClickedEvent.class), new Rect(Point.ZERO, size().get())) && action != null)
                    action.clicked();
            }
        });

        this.getAnimations().addAnimation(new Animation("pressed") {
            @Override
            public void update(double speed) {
                GUIButton button = (GUIButton) object;

                Color source = overrideColor;
                Color end = button.getStyle().getColor("buttonPressed");

                overrideColor = softColorChange(source, end, (float) speed * 2f);

                isRun = !softColorChangeDone(source, end);
            }
        });

        this.getAnimations().addAnimation(new Animation("unpressed") {
            @Override
            public void update(double speed) {
                GUIButton button = (GUIButton) object;

                Color source = overrideColor;
                Color end = button.getStyle().getColor("buttonBackground");

                overrideColor = softColorChange(source, end, (float) speed * 2);

                isRun = !softColorChangeDone(source, end);
            }
        });

        minimumSize().set(margin.minimumSize());
    }

    public GUIButton setAction(ButtonAction action) {
        this.action = action;

        return this;
    }

    public GUIText getText() {
        return text;
    }

    @Override
    public void init(GUIAbstractLayout owner) {
        super.init(owner);

        overrideColor = getStyle().getColor("buttonBackground");
    }

}
