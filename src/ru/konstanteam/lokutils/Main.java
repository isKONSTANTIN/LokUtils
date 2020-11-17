package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.ui.core.maincanvas.UIMainCanvasSystem;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.layout.ListLayout;
import ru.konstanteam.lokutils.ui.layout.ScrollLayout;
import ru.konstanteam.lokutils.ui.objects.*;
import ru.konstanteam.lokutils.ui.objects.margin.Margin;
import ru.konstanteam.lokutils.ui.objects.margin.UIMargin;
import ru.konstanteam.lokutils.ui.objects.slider.UISlider;

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
        this.uiController.getLayout().addObject(new UIMargin(scrollLayout, new Margin(2,2,2,2)), Point.ZERO);

        scrollLayout.addObject(layout, Point.ZERO);
        scrollLayout.size().set(() -> this.uiController.getLayout().size().get().relativeTo(4,4));

        layout.addObject(new UICheckBox());
        layout.addObject(new UILineSpace());
        layout.addObject(new UISeparate());
        layout.addObject(new UITextField());
        layout.addObject(new UISlider());

        for (int i = 0; i < 50; i++){
            layout.addObject(new UIText().setText(String.valueOf(i)));
        }

        window.setWindowCloseCallback((window) -> this.close());
    }

}
