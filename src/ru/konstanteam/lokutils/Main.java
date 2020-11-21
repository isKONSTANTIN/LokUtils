package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.gui.core.maincanvas.UIMainCanvasSystem;
import ru.konstanteam.lokutils.gui.layout.FreeLayout;
import ru.konstanteam.lokutils.gui.layout.ListLayout;
import ru.konstanteam.lokutils.gui.layout.ScrollLayout;
import ru.konstanteam.lokutils.gui.objects.*;
import ru.konstanteam.lokutils.gui.objects.margin.Margin;
import ru.konstanteam.lokutils.gui.objects.margin.GUIMargin;
import ru.konstanteam.lokutils.gui.objects.slider.GUISlider;

public class Main extends Application<UIMainCanvasSystem<FreeLayout>> {
    public Main() {
        super(
                new UIMainCanvasSystem<>(new FreeLayout())
        );
    }

    public static void main(String[] args) {
        new Main().open();
    }

    @Override
    public void initEvent() {
        window.setResizable(true);
        ListLayout layout = new ListLayout();
        ScrollLayout scrollLayout = new ScrollLayout();
        this.uiController.getLayout().addObject(new GUIMargin(scrollLayout, new Margin(2,2,2,2)), Point.ZERO);

        scrollLayout.addObject(layout, Point.ZERO);
        scrollLayout.size().set(() -> this.uiController.getLayout().size().get().relativeTo(4,4));

        layout.addObject(new GUICheckBox());
        layout.addObject(new GUITextField());
        layout.addObject(new GUISlider());
        layout.addObject(new GUILineSpace());

        for (int i = 0; i < 50; i++){
            layout.addObject(new GUIText().setText(String.valueOf(i)));
        }

        window.setWindowCloseCallback((window) -> this.close());
    }

}
