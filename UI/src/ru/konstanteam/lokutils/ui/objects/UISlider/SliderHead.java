package ru.konstanteam.lokutils.ui.objects.UISlider;

import ru.konstanteam.lokutils.objects.Circle;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseMoveEvent;

import static org.lwjgl.opengl.GL11.glColor4f;

public class SliderHead extends UIObject {
    public Color overrideColor;

    public SliderHead(UISlider slider){
        customersContainer.addCustomer(event -> {
            float sliderWidth = slider.size().get().width;
            slider.setHead(event.startPosition.x / sliderWidth + event.deltaPositionChange.x / sliderWidth);
        }, MouseMoveEvent.class);
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
