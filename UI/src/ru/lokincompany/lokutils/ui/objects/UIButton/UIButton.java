package ru.lokincompany.lokutils.ui.objects.UIButton;

import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.animation.Animation;
import ru.lokincompany.lokutils.ui.eventsystem.EventTools;
import ru.lokincompany.lokutils.ui.eventsystem.events.ClickType;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.lokincompany.lokutils.ui.objects.UIPanel;
import ru.lokincompany.lokutils.ui.objects.UIText;

import static ru.lokincompany.lokutils.ui.positioning.AdvancedRect.CENTER;

public class UIButton extends UIPanel {

    protected UIText text;
    protected ButtonAction action;

    public UIButton() {
        this.text = (UIText) new UIText().setText("Button").setPosition(CENTER);
        this.getCanvas().addObject(text);

        customersContainer.addCustomer(event -> {
            if (event.clickType == ClickType.CLICKED && area.getRect().inside(event.position)) {
                getAnimations().stopAll();
                getAnimations().startAnimation("pressed");
            } else{
                getAnimations().addAnimationToTaskList("unpressed");

                if (EventTools.realized(event, customersContainer.getLastEvent(MouseClickedEvent.class), this.area.getRect()) && action != null)
                    action.clicked();
            }

        }, MouseClickedEvent.class);

        this.getAnimations().addAnimation(new Animation("pressed") {
            @Override
            public void update() {
                UIButton button = (UIButton) object;

                Color source = overrideColor;
                Color end = button.getStyle().getColor("buttonPressed");

                overrideColor = softColorChange(source, end, 2);

                isRun = !softColorChangeDone(source, end);
            }
        });

        this.getAnimations().addAnimation(new Animation("unpressed") {
            @Override
            public void update() {
                UIButton button = (UIButton) object;

                Color source = overrideColor;
                Color end = button.getStyle().getColor("buttonBackground");

                overrideColor = softColorChange(source, end, 2);

                isRun = !softColorChangeDone(source, end);
            }
        });

        setSize(new Size(100, 30));
    }

    public UIButton setAction(ButtonAction action){
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
