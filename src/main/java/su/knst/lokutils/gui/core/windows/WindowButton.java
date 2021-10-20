package su.knst.lokutils.gui.core.windows;

import org.lwjgl.opengl.GL11;
import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.gui.animation.Animation;
import su.knst.lokutils.gui.core.windows.window.BaseWindow;
import su.knst.lokutils.gui.eventsystem.EventTools;
import su.knst.lokutils.gui.eventsystem.events.ClickType;
import su.knst.lokutils.gui.eventsystem.events.MouseClickedEvent;
import su.knst.lokutils.objects.Circle;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.render.tools.GLFastTools;

public class WindowButton extends GUIObject {
    protected Circle field;
    protected WindowButtonAction action;
    protected BaseWindow window;

    protected Color overrideColor;
    protected Color pressedColor;
    protected Color defaultColor;

    public WindowButton(Circle field, BaseWindow window, Color pressedColor, Color defaultColor, WindowButtonAction action) {
        this.field = field;
        this.action = action;
        this.window = window;

        this.pressedColor = pressedColor;
        this.defaultColor = defaultColor;
        this.overrideColor = defaultColor;

        size().track(() -> new Size(field.radius * 2, field.radius * 2));

        customersContainer.setCustomer(MouseClickedEvent.class, event -> {
            if (event.clickType == ClickType.CLICKED && field.inside(event.position)) {
                this.getAnimations().stopAll();
                this.getAnimations().startAnimation("pressed");
            } else {
                this.getAnimations().addAnimationToTaskList("unpressed");

                if (EventTools.realized(event, customersContainer.getLastEvent(MouseClickedEvent.class), this.field))
                    this.action.take();
            }

        });

        this.getAnimations().addAnimation(new Animation<WindowButton>("pressed") {
            @Override
            public void update(double speed) {
                Color end = object.pressedColor;

                overrideColor = softColorChange(overrideColor, end, (float) speed * 2f);

                isRun = !softColorChangeDone(overrideColor, end);
            }
        });

        this.getAnimations().addAnimation(new Animation<WindowButton>("unpressed") {
            @Override
            public void update(double speed) {
                Color end = object.defaultColor;

                overrideColor = softColorChange(overrideColor, end, (float) speed * 2f);

                isRun = !softColorChangeDone(overrideColor, end);
            }
        });
    }

    public void render() {
        animations.update(GLContext.getCurrent().getWindow().getMonitor().getVideoMode().refreshRate());

        GL11.glColor4f(overrideColor.red, overrideColor.green, overrideColor.blue, overrideColor.alpha);
        GLFastTools.drawCircle(field);
    }
}
