package ru.konstanteam.lokutils.ui.core.windows;

import org.lwjgl.opengl.GL11;
import ru.konstanteam.lokutils.objects.Circle;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.animation.Animation;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.eventsystem.EventTools;
import ru.konstanteam.lokutils.ui.eventsystem.events.ClickType;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseClickedEvent;

class WindowButton extends UIObject {
    protected Circle field;
    protected WindowButtonAction action;
    protected UIWindow window;

    protected Color overrideColor;
    protected Color pressedColor;
    protected Color defaultColor;

    WindowButton(Circle field, UIWindow window, Color pressedColor, Color defaultColor, WindowButtonAction action) {
        this.field = field;
        this.action = action;
        this.window = window;

        this.pressedColor = pressedColor;
        this.defaultColor = defaultColor;
        this.overrideColor = defaultColor;

        customersContainer.addCustomer(event -> {
            if (event.clickType == ClickType.CLICKED && field.inside(event.position)) {
                this.getAnimations().stopAll();
                this.getAnimations().startAnimation("pressed");
            }else {
                this.getAnimations().addAnimationToTaskList("unpressed");

                if (EventTools.realized(event, customersContainer.getLastEvent(MouseClickedEvent.class), this.field))
                    this.action.take();
            }

        }, MouseClickedEvent.class);

        this.getAnimations().addAnimation(new Animation<WindowButton>("pressed") {
            @Override
            public void update() {
                Color end = object.pressedColor;

                overrideColor = softColorChange(overrideColor, end, 2);

                isRun = !softColorChangeDone(overrideColor, end);
            }
        });

        this.getAnimations().addAnimation(new Animation<WindowButton>("unpressed") {
            @Override
            public void update() {
                Color end = object.defaultColor;

                overrideColor = softColorChange(overrideColor, end, 2);

                isRun = !softColorChangeDone(overrideColor, end);
            }
        });
    }

    public Circle getField(){
        return field;
    }

    public void render(){
        animations.update();

        GL11.glColor4f(overrideColor.red, overrideColor.green, overrideColor.blue, overrideColor.alpha);
        GLFastTools.drawCircle(field);
    }
}
