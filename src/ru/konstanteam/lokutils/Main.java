package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import ru.konstanteam.lokutils.gui.core.windows.GUIWindowSystem;
import ru.konstanteam.lokutils.gui.core.windows.window.BaseWindow;
import ru.konstanteam.lokutils.gui.layout.*;
import ru.konstanteam.lokutils.gui.objects.GUICheckBox;
import ru.konstanteam.lokutils.gui.objects.GUILineSpace;
import ru.konstanteam.lokutils.gui.objects.GUIText;
import ru.konstanteam.lokutils.gui.objects.GUITextField;
import ru.konstanteam.lokutils.gui.objects.button.GUIButton;
import ru.konstanteam.lokutils.gui.objects.margin.GUIMargin;
import ru.konstanteam.lokutils.gui.objects.margin.Margin;
import ru.konstanteam.lokutils.gui.objects.slider.GUISlider;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Window;

import java.util.ArrayList;

public class Main extends Application<GUIMainCanvasSystem> {
    public Main() {
        super(new GUIMainCanvasSystem(), new Window().setIcon(new String[]{"/home/lokin135/LokUtils.png"}));
    }

    public static void main(String[] args) {
        new Main().open();
    }

    @Override
    public void initEvent() {
        window.setResizable(true);

        BaseLayout layout = new BaseLayout();

        layout.addObject(new GUIButton());
        layout.addObject(new GUIButton());
        layout.addObject(new GUIButton());
        layout.addObject(new GUIButton());
        layout.addObject(new GUIButton());
        layout.addObject(new GUIButton());
        layout.addObject(new GUIButton());

        ScrollLayout scrollLayout = new ScrollLayout();
        scrollLayout.addObject(new GUIMargin(layout, new Margin(5, 5, 5, 5)), Point.ZERO);

        scrollLayout.size().set(uiController.getLayout().size());

        scrollLayout.size().addListener((old, newV) -> {
            layout.size().set(layout.minimumSize().get().setWidth(newV.width));
        });

        this.uiController.getLayout().addObject(scrollLayout, Point.ZERO);

        this.window.setWindowCloseCallback((win) -> this.close());
    }

}
