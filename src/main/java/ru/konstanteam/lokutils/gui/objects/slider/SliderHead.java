package ru.konstanteam.lokutils.gui.objects.slider;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.objects.Circle;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.tools.GLFastTools;

import static org.lwjgl.opengl.GL11.glColor4f;

public class SliderHead extends GUIObject {
    public Color overrideColor;

    public SliderHead(GUISlider slider) {
        customersContainer.setCustomer(MouseMoveEvent.class, event -> {
            float sliderWidth = slider.size().get().width;
            slider.setHead(event.startPosition.x / sliderWidth + event.deltaPositionChange.x / sliderWidth);
        });

        minimumSize().set(() -> {
            float radius = getCircleRadius();

            return new Size(radius, radius);
        });
    }

    protected float getCircleRadius() {
        return Math.max(size.get().width, size.get().height) / 2f;
    }

    @Override
    public void render() {
        Color color = overrideColor != null ? overrideColor : getStyle().getColor("sliderHead");
        glColor4f(color.red, color.green, color.blue, color.alpha);

        GLFastTools.drawCircle(new Circle(Point.ZERO, getCircleRadius()));
    }
}
