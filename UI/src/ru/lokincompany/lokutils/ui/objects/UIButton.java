package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.animation.Animation;
import ru.lokincompany.lokutils.ui.animation.Animations;
import ru.lokincompany.lokutils.ui.positioning.Position;
import ru.lokincompany.lokutils.ui.positioning.PositioningSetter;

public class UIButton extends UIObject {

    UIText text;
    UIPanel panel;

    public UIButton() {
        this.panel = (UIPanel) new UIPanel().setPosition(new PositioningSetter(this::getPosition)).setSize(new PositioningSetter(this::getSize));
        this.text = (UIText) new UIText().setText("Button").setPosition(new PositioningSetter(Position.Center));
        this.panel.getCanvas().addObject(text);
/*
        MouseEvent mouseEvent = new MouseEvent();

        mouseEvent.setClickedAction((event) -> {
            event.getUiObject().getAnimations().stopAll();
            event.getUiObject().getAnimations().startAnimation("pressed");
        });

        mouseEvent.setUnClickedAction((event) -> {
            event.getUiObject().getAnimations().stopAll();
            event.getUiObject().getAnimations().startAnimation("unpressed");
        });

        mouseEvent.setPointedAction((event) -> {
            Animations animations = event.getUiObject().getAnimations();
            if (animations.somethingIsRun()) return;

            event.getUiObject().getAnimations().startAnimation("pointed");
        });

        mouseEvent.setUnPointedAction((event) -> {
            Animations animations = event.getUiObject().getAnimations();
            if (animations.somethingIsRun() && !animations.animationIsRun("pointed")) return;

            animations.stopAll();
            animations.startAnimation("unpressed");
        });

        this.getEventHandler().putEvent(mouseEvent); */

        this.getAnimations().addAnimation(new Animation("pressed") {
            @Override
            public void update() {
                Color source = ((UIButton) object).panel.overrideColor;
                Color end = object.getStyle().getColor("buttonPressed");
                softColorChange(source, end, 2);
                isRun = !softColorChangeDone(source, end);
            }
        });

        this.getAnimations().addAnimation(new Animation("pointed") {
            @Override
            public void update() {
                Color source = ((UIButton) object).panel.overrideColor;
                Color end = object.getStyle().getColor("buttonPointed");
                softColorChange(source, end, 2);
                isRun = !softColorChangeDone(source, end);
            }
        });

        this.getAnimations().addAnimation(new Animation("unpressed") {
            @Override
            public void update() {
                Color source = ((UIButton) object).panel.overrideColor;
                Color end = object.getStyle().getColor("buttonBackground");
                softColorChange(source, end, 2);
                isRun = !softColorChangeDone(source, end);
            }
        });

        setSize(new Vector2f(100, 30));
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
