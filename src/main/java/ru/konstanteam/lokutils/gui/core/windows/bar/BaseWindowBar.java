package ru.konstanteam.lokutils.gui.core.windows.bar;

import org.lwjgl.opengl.GL11;
import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.core.windows.WindowButton;
import ru.konstanteam.lokutils.gui.core.windows.window.BaseWindow;
import ru.konstanteam.lokutils.gui.eventsystem.CustomersContainer;
import ru.konstanteam.lokutils.gui.layout.Alignment;
import ru.konstanteam.lokutils.gui.layout.BaseLayout;
import ru.konstanteam.lokutils.gui.layout.FreeLayout;
import ru.konstanteam.lokutils.gui.objects.GUIText;
import ru.konstanteam.lokutils.gui.objects.margin.GUIMargin;
import ru.konstanteam.lokutils.gui.objects.margin.Margin;
import ru.konstanteam.lokutils.gui.style.GUIObjectAsset;
import ru.konstanteam.lokutils.objects.*;
import ru.konstanteam.lokutils.render.tools.GLFastTools;

import java.util.ArrayList;

public class BaseWindowBar<T extends BaseWindow> extends GUIWindowBar<T> {
    protected FreeLayout baseLayout;
    protected BaseLayout buttonsLayout;
    protected GUIText text;
    protected T window;

    protected WindowButton minimizeButton;
    protected WindowButton closeButton;
    protected GUIObjectAsset asset;

    public void render() {
        Color color = asset.color("background");

        GL11.glColor4f(color.red, color.green, color.blue, color.alpha);
        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO, baseLayout.size().get()), 4, GLFastTools.getOptimalRoundingPieces(4), new boolean[]{true, true, window.minimized().get(), window.minimized().get()});

        baseLayout.render();
    }

    @Override
    public Rect getRect() {
        Size size = baseLayout.size().get();

        return new Rect(Point.ZERO, size);
    }

    @Override
    public void init(T window) {
        this.window = window;
        this.asset = window.getStyle().asset(GUIWindowBar.class);

        baseLayout = new FreeLayout();
        baseLayout.size().set(() -> window.contentSize().get().setHeight(14));

        buttonsLayout = new BaseLayout();

        buttonsLayout.size().set(() -> {
            ArrayList<GUIObject> objects = buttonsLayout.getObjects();
            float width = 0;
            float maxHeight = 0;

            for (GUIObject object : objects) {
                Size objectSize = object.size().get();
                width += objectSize.width + buttonsLayout.getGap();
                maxHeight = Math.max(maxHeight, objectSize.height);
            }

            return new Size(width, maxHeight);
        });

        minimizeButton = new WindowButton(new Circle(Point.ZERO, 5), window,
                asset.color("minimizeButtonPressed"),
                asset.color("minimizeButtonBackground"),
                () -> window.minimized().set(!window.minimized().get())
        );
        minimizeButton.setName("minimizeButton");

        closeButton = new WindowButton(new Circle(Point.ZERO, 5),
                window,
                asset.color("closeButtonPressed"),
                asset.color("closeButtonBackground"),
                () -> {
                    window.getWindowSystem().closeWindow(window);
                }
        );
        closeButton.setName("closeButton");

        buttonsLayout.addObject(minimizeButton);

        text = new GUIText().setStyleFontName("windowTitle");

        baseLayout.addObject(new GUIMargin(text, new Margin(buttonsLayout.getGap(), 0, 0, 0)), Alignment.CENTER_LEFT);
        baseLayout.addObject(buttonsLayout, Alignment.CENTER_RIGHT);

        minimumSize.set(() -> buttonsLayout.size().get().offset(text.size().get()).offset(30, 0).setHeight(14));
    }

    @Override
    public void update() {
        boolean closeButtonInLayout = buttonsLayout.getObject("closeButton") != null;

        if (window.closable().get() && !closeButtonInLayout)
            buttonsLayout.addObject(closeButton);
        else if (!window.closable().get() && closeButtonInLayout)
            buttonsLayout.removeObject(closeButton);

        baseLayout.update();
    }

    @Override
    public CustomersContainer getCustomersContainer() {
        return baseLayout.getCustomersContainer();
    }

    @Override
    public String getTitle() {
        return text.string().get();
    }

    @Override
    public void setTitle(String text) {
        this.text.string().set(text);
    }
}
