package ru.konstanteam.lokutils.ui.core.windows.bar;

import org.lwjgl.opengl.GL11;
import ru.konstanteam.lokutils.objects.*;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.core.windows.WindowButton;
import ru.konstanteam.lokutils.ui.core.windows.window.BaseWindow;
import ru.konstanteam.lokutils.ui.eventsystem.CustomersContainer;
import ru.konstanteam.lokutils.ui.layout.Alignment;
import ru.konstanteam.lokutils.ui.layout.BaseLayout;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.objects.UIText;

import java.util.ArrayList;

public class BaseWindowBar extends AbstractWindowBar {
    protected FreeLayout baseLayout;
    protected BaseLayout buttonsLayout;
    protected UIText text;
    protected BaseWindow window;

    public void render() {
        Color color = window.getStyle().getColor("windowBarBackground");

        GL11.glColor4f(color.red, color.green, color.blue, color.alpha);
        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO, baseLayout.size().get()), 4, GLFastTools.getOptimalRoundingPieces(4), new boolean[]{true, true, window.isMinimized(), window.isMinimized()});

        baseLayout.render();
    }

    @Override
    public Rect getRect() {
        Size size = baseLayout.size().get();

        return new Rect(new Point(0, -size.height), size);
    }

    @Override
    public void init(BaseWindow window) {
        this.window = window;
        baseLayout = new FreeLayout();
        baseLayout.size().set(() -> window.getContentSize().setHeight(14));

        buttonsLayout = new BaseLayout();

        buttonsLayout.size().set(() -> {
            ArrayList<UIObject> objects = buttonsLayout.getObjects();
            float width = 0;
            float maxHeight = 0;

            for (UIObject object : objects) {
                Size objectSize = object.size().get();
                width += objectSize.width + buttonsLayout.getGap();
                maxHeight = Math.max(maxHeight, objectSize.height);
            }

            return new Size(width, maxHeight);
        });

        buttonsLayout.addObject(
                new WindowButton(new Circle(Point.ZERO, 5),
                        window,
                        window.getStyle().getColor("windowMinimizeButtonPressed"),
                        window.getStyle().getColor("windowMinimizeButtonBackground"),
                        () -> {
                            window.setMinimized(!window.isMinimized());
                        }
                )
        );

        buttonsLayout.addObject(
                new WindowButton(new Circle(Point.ZERO, 5),
                        window,
                        window.getStyle().getColor("windowCloseButtonPressed"),
                        window.getStyle().getColor("windowCloseButtonBackground"),
                        () -> {
                            window.getWindowSystem().closeWindow(window);
                        }
                )
        );

        text = new UIText().setStyleFontName("windowTitle");

        baseLayout.addObject(text, Alignment.CENTER_LEFT);
        baseLayout.addObject(buttonsLayout, Alignment.CENTER_RIGHT);
    }

    @Override
    public void update() {
        baseLayout.update(null);
    }

    @Override
    public CustomersContainer getCustomersContainer() {
        return baseLayout.getCustomersContainer();
    }

    @Override
    public String getTitle() {
        return text.getText();
    }

    @Override
    public void setTitle(String text) {
        this.text.setText(text);
    }
}
