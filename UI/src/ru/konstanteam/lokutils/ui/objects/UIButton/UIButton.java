package ru.konstanteam.lokutils.ui.objects.UIButton;

import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.animation.Animation;
import ru.konstanteam.lokutils.ui.eventsystem.EventTools;
import ru.konstanteam.lokutils.ui.eventsystem.events.ClickType;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.objects.UIPanel;
import ru.konstanteam.lokutils.ui.objects.UIText;

import static ru.konstanteam.lokutils.ui.layout.Alignment.CENTER;

public class UIButton extends UIPanel<FreeLayout> {

    protected UIText text;
    protected ButtonAction action;

    public UIButton() {
        super(new FreeLayout());
        this.text = new UIText().setText("Button");
        this.getRootLayout().addObject(text, CENTER);

        customersContainer.addCustomer(event -> {
            if (event.clickType == ClickType.CLICKED && new Rect(Point.ZERO, size().get()).inside(event.position)) {
                getAnimations().stopAll();
                getAnimations().startAnimation("pressed");
            } else {
                getAnimations().addAnimationToTaskList("unpressed");

                if (EventTools.realized(event, customersContainer.getLastEvent(MouseClickedEvent.class), new Rect(Point.ZERO, size().get())) && action != null)
                    action.clicked();
            }

        }, MouseClickedEvent.class);

        this.getAnimations().addAnimation(new Animation("pressed") {
            @Override
            public void update(double speed) {
                UIButton button = (UIButton) object;

                Color source = overrideColor;
                Color end = button.getStyle().getColor("buttonPressed");

                overrideColor = softColorChange(source, end, (float)speed * 2f);

                isRun = !softColorChangeDone(source, end);
            }
        });

        this.getAnimations().addAnimation(new Animation("unpressed") {
            @Override
            public void update(double speed) {
                UIButton button = (UIButton) object;

                Color source = overrideColor;
                Color end = button.getStyle().getColor("buttonBackground");

                overrideColor = softColorChange(source, end, (float)speed * 2);

                isRun = !softColorChangeDone(source, end);
            }
        });

        size().set(new Size(100, 30));
    }

    public UIButton setAction(ButtonAction action) {
        this.action = action;

        return this;
    }

    public UIText getText() {
        return text;
    }

    @Override
    public void init(UIObject parent) {
        super.init(parent);

        overrideColor = getStyle().getColor("buttonBackground");
    }

}
