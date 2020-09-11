package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.ui.core.maincanvas.UIMainCanvasSystem;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;

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
        window.setWindowCloseCallback((window) -> this.close());
    }

}
