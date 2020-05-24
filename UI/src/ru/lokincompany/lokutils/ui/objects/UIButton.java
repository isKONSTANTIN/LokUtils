package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.animation.Animation;
import ru.lokincompany.lokutils.ui.eventsystem.events.ClickType;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.lokincompany.lokutils.ui.positioning.PositioningLink;

import static ru.lokincompany.lokutils.ui.positioning.AdvancedRect.CENTER;

public class UIButton extends UIObject {

    protected UIText text;
    protected UIPanel panel;

    public UIButton() {
        this.panel = (UIPanel) new UIPanel().bindArea(this.getArea());
        this.text = (UIText) new UIText().setText("Button").setPosition(CENTER);
        this.panel.getCanvas().addObject(text);

        customersContainer.addCustomer(event -> {
            if (event.clickType == ClickType.CLICKED) {
                getAnimations().stopAll();
                getAnimations().startAnimation("pressed");
            } else
                getAnimations().addAnimationToTaskList("unpressed");
        }, MouseClickedEvent.class);

        this.getAnimations().addAnimation(new Animation("pressed") {
            @Override
            public void update() {
                UIButton button = (UIButton) object;

                Color source = button.panel.overrideColor;
                Color end = button.getStyle().getColor("buttonPressed");

                button.panel.overrideColor = softColorChange(source, end, 2);

                isRun = !softColorChangeDone(source, end);
            }
        });

        this.getAnimations().addAnimation(new Animation("unpressed") {
            @Override
            public void update() {
                UIButton button = (UIButton) object;

                Color source = button.panel.overrideColor;
                Color end = object.getStyle().getColor("buttonBackground");

                button.panel.overrideColor = softColorChange(source, end, 2);

                isRun = !softColorChangeDone(source, end);
            }
        });

        setSize(new Size(100, 30));
    }

    public UIText getText() {
        return text;
    }

    public UIPanel getPanel() {
        return panel;
    }

    @Override
    public void init(UIObject parent) {
        super.init(parent);

        panel.overrideColor = getStyle().getColor("buttonBackground");
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent);

        panel.update(this);
    }
}
