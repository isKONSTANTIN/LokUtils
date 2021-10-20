package su.knst.lokutils.gui.objects.slider;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.gui.eventsystem.events.MouseMoveEvent;
import su.knst.lokutils.objects.Circle;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.tools.GLFastTools;

import static org.lwjgl.opengl.GL11.glColor4f;

public class SliderHead extends GUIObject {
    public Color overrideColor;

    public SliderHead(GUISlider slider) {
        customersContainer.setCustomer(MouseMoveEvent.class, event -> {
            float sliderWidth = slider.size().get().width;
            slider.setHead(event.startPosition.x / sliderWidth + event.deltaPositionChange.x / sliderWidth);
        });

        minimumSize().track(() -> {
            float radius = getCircleRadius();

            return new Size(radius, radius);
        });
    }

    protected float getCircleRadius() {
        return Math.max(size.get().width, size.get().height) / 2f;
    }

    @Override
    public void render() {
        Color color = overrideColor != null ? overrideColor : asset.color("head");
        glColor4f(color.red, color.green, color.blue, color.alpha);

        GLFastTools.drawCircle(new Circle(Point.ZERO, getCircleRadius()));
    }
}
