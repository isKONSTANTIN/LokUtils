package ru.konstanteam.lokutils.gui.objects.slider;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.gui.layout.GUIAbstractLayout;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.render.tools.GLFastTools;

import static org.lwjgl.opengl.GL11.glColor4f;

public class GUISlider extends GUIObject {
    protected SliderHead head;
    protected float fullness;

    public GUISlider() {
        this.head = new SliderHead(this);
        this.head.size().track(() -> new Size(0, size.get().height / 1.5f));

        customersContainer.setCustomer(MouseMoveEvent.class, (event) -> {
            if (new Rect(Point.ZERO, size.get()).inside(event.startPosition))
                this.head.getCustomersContainer().handle(event);
        });

        minimumSize().track(head.minimumSize());

        size().set(new Size(100, 26));
    }

    protected Point getHeadPosition() {
        Size headSize = head.size().get();
        Size mineSize = size.get();
        float headRadius = head.getCircleRadius();

        return new Point(Math.min(Math.max(headSize.width / 2f + mineSize.width * fullness - headRadius, 0.0f), mineSize.width - headRadius * 2f), mineSize.height / 2f - headSize.height / 2f);
    }

    public void moveHead(float delta) {
        fullness += delta;

        fullness = Math.max(Math.min(fullness, 1), 0);
    }

    public void setHead(float delta) {
        fullness = delta;

        fullness = Math.max(Math.min(fullness, 1), 0);
    }

    public float getFullness() {
        return fullness;
    }

    @Override
    public void init(GUIAbstractLayout owner) {
        super.init(owner);

        head.init(owner);
    }

    public void render() {
        Point headPosition = getHeadPosition();
        Size size = this.size.get();

        Color colorBackground = asset.color("background");
        Point backgroundPosition = new Point(0, size.height / 2f - size.height / 6f);
        glColor4f(colorBackground.red, colorBackground.green, colorBackground.blue, colorBackground.alpha);
        GLFastTools.drawRoundedSquare(new Rect(backgroundPosition, size.setHeight(size.height / 3f)), 1);

        Color colorFullness = asset.color("fullness");
        glColor4f(colorFullness.red, colorFullness.green, colorFullness.blue, colorFullness.alpha);
        GLFastTools.drawRoundedSquare(new Rect(backgroundPosition, new Size(Math.max(size.width * fullness, size.height / 3f), size.height / 3f)), 1);

        GLContext.getCurrent().getViewTools().pushTranslate(headPosition);
        head.render();
        GLContext.getCurrent().getViewTools().popTranslate();
    }
}
