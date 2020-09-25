package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.render.Texture;
import ru.konstanteam.lokutils.ui.core.maincanvas.UIMainCanvasSystem;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.layout.ListLayout;
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
        this.uiController.getLayout().addObject(new UICheckBox());
        this.uiController.getLayout().addObject(new UILineSpace());
        this.uiController.getLayout().addObject(new UISeparate());
        this.uiController.getLayout().addObject(new UIText().setText("Hello"));
        this.uiController.getLayout().addObject(new UITextField());
        this.uiController.getLayout().addObject(new UISlider());
        window.setWindowCloseCallback((window) -> this.close());
    }

}
