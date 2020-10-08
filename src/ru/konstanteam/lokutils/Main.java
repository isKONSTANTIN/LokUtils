package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.render.Texture;
import ru.konstanteam.lokutils.ui.core.maincanvas.UIMainCanvasSystem;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.layout.ListLayout;
import ru.konstanteam.lokutils.ui.layout.ScrollLayout;
import ru.konstanteam.lokutils.ui.objects.*;
import ru.konstanteam.lokutils.ui.objects.UISlider.UISlider;

public class Main extends Application<UIMainCanvasSystem<ListLayout>> {
    public Main() {
        super(
                new UIMainCanvasSystem<>(new ListLayout())
        );
    }

    public static void main(String[] args) {
        new Main().open();
    }

    @Override
    public void initEvent() {
        ListLayout layout = new ListLayout();
        ScrollLayout scrollLayout = new ScrollLayout();
        this.uiController.getLayout().addObject(scrollLayout);
        scrollLayout.addObject(layout, Point.ZERO);

        layout.addObject(new UICheckBox());
        layout.addObject(new UILineSpace());
        layout.addObject(new UISeparate());
        layout.addObject(new UIText().setText("Hello"));
        layout.addObject(new UITextField());
        layout.addObject(new UISlider());

        window.setWindowCloseCallback((window) -> this.close());
    }

}
