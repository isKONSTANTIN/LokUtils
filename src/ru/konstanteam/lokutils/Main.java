package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.gui.core.windows.GUIWindowSystem;
import ru.konstanteam.lokutils.gui.core.windows.window.BaseWindow;
import ru.konstanteam.lokutils.gui.layout.ListLayout;
import ru.konstanteam.lokutils.gui.layout.ScrollLayout;
import ru.konstanteam.lokutils.gui.objects.GUICheckBox;
import ru.konstanteam.lokutils.gui.objects.GUILineSpace;
import ru.konstanteam.lokutils.gui.objects.GUIText;
import ru.konstanteam.lokutils.gui.objects.GUITextField;
import ru.konstanteam.lokutils.gui.objects.slider.GUISlider;
import ru.konstanteam.lokutils.objects.Point;

public class Main extends Application<GUIWindowSystem> {
    public Main() {
        super(new GUIWindowSystem());
    }

    public static void main(String[] args) {
        new Main().open();
    }

    void addWindow() {
        ListLayout layout = new ListLayout();
        ScrollLayout scrollLayout = new ScrollLayout();
        scrollLayout.addObject(layout, Point.ZERO);

        BaseWindow window = new BaseWindow();
        this.uiController.addWindow(window);
        window.getBar().setTitle("Test : 3");
        window.resizable().set(true);
        window.closable().set(true);

        scrollLayout.size().set(window.contentSize());
        window.getLayout().addObject(scrollLayout, Point.ZERO);
        window.resizable().set(true);

        layout.addObject(new GUICheckBox());
        layout.addObject(new GUITextField());
        layout.addObject(new GUISlider());
        layout.addObject(new GUILineSpace());

        for (int i = 0; i < 50; i++) {
            layout.addObject(new GUIText().setText(String.valueOf(i)));
        }

    }

    @Override
    public void initEvent() {
        window.setResizable(true);

        addWindow();
        addWindow();

        this.window.setWindowCloseCallback((win) -> this.close());
    }

}
