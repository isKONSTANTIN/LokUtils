package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import ru.konstanteam.lokutils.gui.layout.*;
import ru.konstanteam.lokutils.gui.objects.button.GUIButton;
import ru.konstanteam.lokutils.gui.objects.margin.GUIMargin;
import ru.konstanteam.lokutils.gui.objects.margin.Margin;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;

public class Main extends Application<GUIMainCanvasSystem> {
    BaseLayout layout;

    public Main() {
        super(new GUIMainCanvasSystem());
    }

    public static void main(String[] args) {
        new Main().open();
    }

    @Override
    public void initEvent() {
        window.setResizable(true);

        layout = new BaseLayout();

        layout.addObject(new GUIButton());
        layout.addObject(new GUIButton());
        layout.addObject(new GUIButton());
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
            layout.size().set(new Size(newV.width, layout.minimumSize().get().height));
        });

        this.uiController.getLayout().addObject(scrollLayout, Point.ZERO);

        this.window.setWindowCloseCallback((win) -> this.close());
    }

}
