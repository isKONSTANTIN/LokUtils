package ru.konstanteam.lokutils.ui.objects.UISlider;

import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.GLContext;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.eventsystem.Event;
import ru.konstanteam.lokutils.ui.eventsystem.events.MouseMoveEvent;

import java.util.concurrent.CompletableFuture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public class UISlider extends UIObject {
    protected SliderHead head;
    protected float fullness;

    public UISlider() {
        this.head = new SliderHead(this);
        this.head.size().set(() -> size.get().setWidth(0));

        customersContainer.addCustomer((event) ->{
            if (new Rect(Point.ZERO, size.get()).inside(event.startPosition))
                this.head.getCustomersContainer().handle(event);
        }, MouseMoveEvent.class);

        size.set(new Size(100,12));
    }

    protected Point getHeadPosition() {
        Size headSize = head.size().get();
        Size mineSize = size.get();
        float headRadius = head.getCircleRadius();

        return new Point(Math.min(Math.max(headSize.width / 2f + mineSize.width * fullness - headRadius, 0), mineSize.width - headRadius * 2f), mineSize.height / 2f - headSize.height / 2f);
    }

    public void moveHead(float delta) {
        fullness += delta;

        fullness = Math.max(Math.min(fullness, 1),0);
    }

    public void setHead(float delta) {
        fullness = delta;

        fullness = Math.max(Math.min(fullness, 1),0);
    }

    public float getFullness() {
        return fullness;
    }

    public void render() {
        Point headPosition = getHeadPosition();
        Size size = this.size.get();

        Color colorFullness = getStyle().getColor("sliderFullness");
        glColor4f(colorFullness.red, colorFullness.green, colorFullness.blue, colorFullness.alpha);

        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO, size.setWidth(Math.max(size.width * fullness, size.height))), 1);

        Color colorEdges = getStyle().getColor("sliderEdges");
        glColor4f(colorEdges.red, colorEdges.green, colorEdges.blue, colorEdges.alpha);

        GLFastTools.drawRoundedHollowSquare(new Rect(Point.ZERO, size), 1);

        GLContext.getCurrent().getViewTools().pushTranslate(headPosition);
        head.render();
        GLContext.getCurrent().getViewTools().popTranslate();
    }
}
